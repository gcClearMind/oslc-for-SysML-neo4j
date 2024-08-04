
/**
 * 通过Id查询资源
 */
function searchById() {
    const id = document.getElementById('id-search').value;
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.match(/\/services\/([^\/]+)\/Blocks/);
    const baseUrl = 'http://localhost:8081/oslc/services/' + productId + '/Blocks/queryResource?oslc.where=';
    const queryParam = 'oslc_neo:id=' + id;
    const encodedQueryParam = encodeURIComponent(queryParam);  //对url进行编码
    const queryUrl =  baseUrl + encodedQueryParam;   // 构建完整的查询URL

    fetch(queryUrl, {
        method: 'POST',
        headers: {
            //'Accept': 'application/rdf+xml',
            // 如果请求体包含数据，需要在这里设置对应的Content-Type
            // 'Content-Type': 'application/rdf+xml'
        },
        // 如果需要附加额外的查询信息，可以放在body中
        // body: ...
    })
        .then(response => response.json())
        .then(data => {
             //向后端接口请求解析查询到的RDF结果
            fetch("http://localhost:8081/oslc/services/" + productId + "/Blocks/parseQueryRdfResult",{
                method: 'POST',
                body: data.data
            })
                .then(response => response.json())
                .then(data => {
                    displayResults(data.data);
                });
        })

}



/**
 * 展示查询到的资源结果
 * @param results
 */
function displayResults(results) {
    const resultsList = document.getElementById('results-list');
    resultsList.innerHTML = '';
    results.forEach(item => {
        const listItem = document.createElement('li');
        const link = document.createElement('a');
        const productId = item.serviceProvider.match(/\/serviceProviders\/([^\/]+)/);
        link.href = "http://localhost:8081/oslc/services/" + productId + "/Blocks/smallPreview/" + item.id;
        link.textContent = item.text + ": " + item.about;
        listItem.appendChild(link);
        resultsList.appendChild(listItem);
    });
}


/**
 * 根据关键词查询，可进行模糊的的文本查询和限定的ID查询
 */
function searchByKeyword(){
    pageQuery(1)   //展示查询结果第一页
}


/**
 * 页面加载完成时执行
 */
document.addEventListener('DOMContentLoaded', function() {
    fetchSelectData();
});


/**
 * 获取下拉框可选的关联Id值
 */
function fetchSelectData() {
    fetch("http://localhost:8081/Excel/getAllResourcesIDs")
        .then(response => response.json())
        .then(data => {
            const selectElement = document.getElementById('associated-id-select');
            selectElement.innerHTML = '<option value="none">None</option>';

            data.data.forEach(id => {
                const option = document.createElement('option');
                option.value = id;
                option.textContent = id;
                selectElement.appendChild(option);
            });
            selectElement.value = 'none';
        });
}


/**
 * 更新查询到的资源的分页展示
 */
function updatePagination(total, pageSize, currentPage) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';
    const totalPages = Math.ceil(total / pageSize);

    const prevButton = document.createElement('button');
    prevButton.textContent = 'Previous';
    prevButton.disabled = currentPage === 1;
    prevButton.onclick = () => pageQuery(currentPage - 1);
    pagination.appendChild(prevButton);

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        if (i === currentPage) {
            pageButton.classList.add('active');
        }
        pageButton.onclick = () => pageQuery(i);
        pagination.appendChild(pageButton);
    }

    const nextButton = document.createElement('button');
    nextButton.textContent = 'Next';
    nextButton.disabled = currentPage === totalPages;
    nextButton.onclick = () => pageQuery(currentPage + 1);
    pagination.appendChild(nextButton);
}



function pageQuery(page) {
    // 定义请求的 URL 和参数
    const baseUrl = 'http://localhost:8080/Excel/AdvancedQuery';
    const searchTerms = document.getElementById('keyword-search').value;
    const limitId = document.getElementById('associated-id-select').value;
    const params = {
        //'oslc.where': 'oslc_ex:associatedId=150&oslc_ex:text=hhh',
        //'oslc.searchTerms': '"database","performance"',
        //'oslc.paging':'true',
        'oslc.where': 'oslc_ex:associatedId=' + limitId,
        'oslc.searchTerms': searchTerms,
        'oslc.page': page
    };

    //构建查询字符串, Object.entries(params) 用于将参数对象转换为一个数组，每个数组元素都是一个包含键和值的数组
    //encodeURIComponent 用于对每个键和值进行编码，确保它们在查询字符串中是安全的
    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const url = `${baseUrl}?${queryString}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            updatePagination(data.data.totalCount, data.data.pageSize, page)
            // 使用获取到的数据
            fetch("http://localhost:8080/Excel/parseQueryRdfResult",{
                method: 'POST',
                body: data.data.rdfData
            })
                .then(response => response.json())
                .then(data => {
                    displayResults(data.data);
                });
        });

}





