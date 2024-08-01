package com.example.oslc.servlet;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.lyo.oslc4j.core.annotation.*;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreMissingAnnotationException;
import org.eclipse.lyo.oslc4j.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class ServiceProviderFactory {
    private static final Logger log = LoggerFactory.getLogger(org.eclipse.lyo.oslc4j.core.model.ServiceProviderFactory.class);

    private ServiceProviderFactory() {
    }

    public static ServiceProvider createServiceProvider(String baseURI, String genericBaseURI, String title, String description, Publisher publisher, Class<?>[] resourceClasses) throws OslcCoreApplicationException, URISyntaxException {
        return initServiceProvider(new ServiceProvider(), baseURI, genericBaseURI, title, description, publisher, resourceClasses, (Map) null);
    }

    public static ServiceProvider createServiceProvider(String baseURI, String genericBaseURI, String title, String description, Publisher publisher, Class<?>[] resourceClasses, Map<String, Object> pathParameterValues) throws OslcCoreApplicationException, URISyntaxException {
        return initServiceProvider(new ServiceProvider(), baseURI, genericBaseURI, title, description, publisher, resourceClasses, pathParameterValues);
    }

    public static ServiceProvider initServiceProvider(ServiceProvider serviceProvider, String baseURI, String genericBaseURI, String title, String description, Publisher publisher, Class<?>[] resourceClasses, Map<String, Object> pathParameterValues) throws OslcCoreApplicationException, URISyntaxException {
        serviceProvider.setTitle(title);
        serviceProvider.setDescription(description);
        serviceProvider.setPublisher(publisher);
        Map<String, Service> serviceMap = new HashMap();
        Class[] var9 = resourceClasses;
        int var10 = resourceClasses.length;

        for (int var11 = 0; var11 < var10; ++var11) {
            Class<?> resourceClass = var9[var11];
            OslcService serviceAnnotation = (OslcService) resourceClass.getAnnotation(OslcService.class);
            if (serviceAnnotation == null) {
                throw new OslcCoreMissingAnnotationException(resourceClass, OslcService.class);
            }

            String domain = serviceAnnotation.value();
            Service service = (Service) serviceMap.get(domain);
            if (service == null) {
                service = new Service(new URI(domain));
                serviceMap.put(domain, service);
            }

            Map<String, Object> initPathParameterValues = pathParameterValues;
            if (pathParameterValues == null) {
                log.warn("pathParameterValues passed to ServiceProviderFactory.initServiceProvider() SHALL NOT be null");
                initPathParameterValues = new HashMap();
            }

            handleResourceClass(baseURI, genericBaseURI, resourceClass, service, (Map) initPathParameterValues);
        }

        Iterator var18 = serviceMap.values().iterator();

        while (var18.hasNext()) {
            Service service = (Service) var18.next();
            serviceProvider.addService(service);
        }

        return serviceProvider;
    }

    private static void handleResourceClass(String baseURI, String genericBaseURI, Class<?> resourceClass, Service service, Map<String, Object> pathParameterValues) throws URISyntaxException {
        Method[] var5 = resourceClass.getMethods();
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            GetMapping getAnnotation = (GetMapping) method.getAnnotation(GetMapping.class);
            int var16;
            OslcDialog[] dialogs;
            if (getAnnotation != null) {
                OslcQueryCapability queryCapabilityAnnotation = (OslcQueryCapability) method.getAnnotation(OslcQueryCapability.class);
                String[] resourceShapes = null;
                if (queryCapabilityAnnotation != null) {
                    service.addQueryCapability(createQueryCapability(baseURI, method, pathParameterValues));
                    String resourceShape = queryCapabilityAnnotation.resourceShape();
                    if (resourceShape != null && resourceShape.length() > 0) {
                        resourceShapes = new String[]{resourceShape};
                    }
                }

                OslcDialogs dialogsAnnotation = (OslcDialogs) method.getAnnotation(OslcDialogs.class);
                if (dialogsAnnotation != null) {
                    dialogs = dialogsAnnotation.value();
                    int var26 = dialogs.length;

                    for (var16 = 0; var16 < var26; ++var16) {
                        OslcDialog dialog = dialogs[var16];
                        if (dialog != null) {
                            service.addSelectionDialog(createSelectionDialog(baseURI, genericBaseURI, method, dialog, resourceShapes, pathParameterValues));
                        }
                    }
                } else {
                    OslcDialog dialogAnnotation = (OslcDialog) method.getAnnotation(OslcDialog.class);
                    if (dialogAnnotation != null) {
                        service.addSelectionDialog(createSelectionDialog(baseURI, genericBaseURI, method, dialogAnnotation, resourceShapes, pathParameterValues));
                    }
                }
            } else {
                PostMapping postAnnotation = (PostMapping) method.getAnnotation(PostMapping.class);
                if (postAnnotation != null) {
                    OslcCreationFactory creationFactoryAnnotation = (OslcCreationFactory) method.getAnnotation(OslcCreationFactory.class);
                    String[] resourceShapes = null;
                    if (creationFactoryAnnotation != null) {
                        service.addCreationFactory(createCreationFactory(baseURI, pathParameterValues, method));
                        resourceShapes = creationFactoryAnnotation.resourceShapes();
                    }

                    OslcDialogs dialogsAnnotation = (OslcDialogs) method.getAnnotation(OslcDialogs.class);
                    if (dialogsAnnotation != null) {
                        dialogs = dialogsAnnotation.value();
                        OslcDialog[] var15 = dialogs;
                        var16 = dialogs.length;

                        for (int var17 = 0; var17 < var16; ++var17) {
                            OslcDialog dialog = var15[var17];
                            if (dialog != null) {
                                service.addCreationDialog(createCreationDialog(baseURI, genericBaseURI, method, dialog, resourceShapes, pathParameterValues));
                            }
                        }
                    } else {
                        OslcDialog dialogAnnotation = (OslcDialog) method.getAnnotation(OslcDialog.class);
                        if (dialogAnnotation != null) {
                            service.addCreationDialog(createCreationDialog(baseURI, genericBaseURI, method, dialogAnnotation, resourceShapes, pathParameterValues));
                        }
                    }
                }
            }
        }

    }

    static CreationFactory createCreationFactory(String baseURI, Map<String, Object> pathParameterValues, Method method) throws URISyntaxException {
        RequestMapping classPathAnnotation = (RequestMapping) method.getDeclaringClass().getAnnotation(RequestMapping.class);
        OslcCreationFactory creationFactoryAnnotation = (OslcCreationFactory) method.getAnnotation(OslcCreationFactory.class);
        RequestMapping methodPathAnnotation = (RequestMapping) method.getAnnotation(RequestMapping.class);
        CreationFactory creationFactory = createCreationFactory(baseURI, pathParameterValues, classPathAnnotation, creationFactoryAnnotation, methodPathAnnotation);
        return creationFactory;
    }

    static CreationFactory createCreationFactory(String baseURI, Map<String, Object> pathParameterValues, RequestMapping classPathAnnotation, OslcCreationFactory creationFactoryAnnotation, RequestMapping methodPathAnnotation) throws URISyntaxException {
        String title = creationFactoryAnnotation.title();
        String label = creationFactoryAnnotation.label();
        String[] resourceShapes = creationFactoryAnnotation.resourceShapes();
        String[] resourceTypes = creationFactoryAnnotation.resourceTypes();
        String[] usages = creationFactoryAnnotation.usages();
        String basePath = baseURI + "/";
        String creation = resolvePathParameters(basePath, pathAnnotationStringValue(classPathAnnotation), pathAnnotationStringValue(methodPathAnnotation), pathParameterValues);
        CreationFactory creationFactory = new CreationFactory(title, (new URI(creation)).normalize());
        if (label != null && label.length() > 0) {
            creationFactory.setLabel(label);
        }

        String[] var13 = resourceShapes;
        int var14 = resourceShapes.length;

        int var15;
        String usage;
        for (var15 = 0; var15 < var14; ++var15) {
            usage = var13[var15];
            creationFactory.addResourceShape((new URI(basePath + usage)).normalize());
        }

        var13 = resourceTypes;
        var14 = resourceTypes.length;

        for (var15 = 0; var15 < var14; ++var15) {
            usage = var13[var15];
            creationFactory.addResourceType(new URI(usage));
        }

        var13 = usages;
        var14 = usages.length;

        for (var15 = 0; var15 < var14; ++var15) {
            usage = var13[var15];
            creationFactory.addUsage(new URI(usage));
        }

        return creationFactory;
    }

    private static String pathAnnotationStringValue(RequestMapping pathAnnotation) {
        if(pathAnnotation == null || pathAnnotation.value().length == 0) {
            return null;
        }
        else {
            String[]  values = pathAnnotation.value();
            return values[0];
        }

    }

    private static QueryCapability createQueryCapability(String baseURI, Method method, Map<String, Object> pathParameterValues) throws URISyntaxException {
        OslcQueryCapability queryCapabilityAnnotation = (OslcQueryCapability) method.getAnnotation(OslcQueryCapability.class);
        String title = queryCapabilityAnnotation.title();
        String label = queryCapabilityAnnotation.label();
        String resourceShape = queryCapabilityAnnotation.resourceShape();
        String[] resourceTypes = queryCapabilityAnnotation.resourceTypes();
        String[] usages = queryCapabilityAnnotation.usages();
        String basePath = baseURI + "/";
        RequestMapping classPathAnnotation = (RequestMapping) method.getDeclaringClass().getAnnotation(RequestMapping.class);
        RequestMapping methodPathAnnotation = (RequestMapping) method.getAnnotation(RequestMapping.class);
        String query = resolvePathParameters(basePath, pathAnnotationStringValue(classPathAnnotation), pathAnnotationStringValue(methodPathAnnotation), pathParameterValues);
        QueryCapability queryCapability = new QueryCapability(title, (new URI(query)).normalize());
        if (label != null && label.length() > 0) {
            queryCapability.setLabel(label);
        }

        if (resourceShape != null && resourceShape.length() > 0) {
            queryCapability.setResourceShape((new URI(basePath + resourceShape)).normalize());
        }

        String[] var14 = resourceTypes;
        int var15 = resourceTypes.length;

        int var16;
        String usage;
        for (var16 = 0; var16 < var15; ++var16) {
            usage = var14[var16];
            queryCapability.addResourceType(new URI(usage));
        }

        var14 = usages;
        var15 = usages.length;

        for (var16 = 0; var16 < var15; ++var16) {
            usage = var14[var16];
            queryCapability.addUsage(new URI(usage));
        }

        return queryCapability;
    }

    private static Dialog createCreationDialog(String baseURI, String genericBaseURI, Method method, OslcDialog dialogAnnotation, String[] resourceShapes, Map<String, Object> pathParameterValues) throws URISyntaxException {
        return createDialog(baseURI, genericBaseURI, "Creation", "creation", method, dialogAnnotation, resourceShapes, pathParameterValues);
    }

    private static Dialog createSelectionDialog(String baseURI, String genericBaseURI, Method method, OslcDialog dialogAnnotation, String[] resourceShapes, Map<String, Object> pathParameterValues) throws URISyntaxException {
        return createDialog(baseURI, genericBaseURI, "Selection", "queryBase", method, dialogAnnotation, resourceShapes, pathParameterValues);
    }

    private static Dialog createDialog(String baseURI, String genericBaseURI, String dialogType, String parameterName, Method method, OslcDialog dialogAnnotation, String[] resourceShapes, Map<String, Object> pathParameterValues) throws URISyntaxException {
        String title = dialogAnnotation.title();
        String label = dialogAnnotation.label();
        String dialogURI = dialogAnnotation.uri();
        String hintWidth = dialogAnnotation.hintWidth();
        String hintHeight = dialogAnnotation.hintHeight();
        String[] resourceTypes = dialogAnnotation.resourceTypes();
        String[] usages = dialogAnnotation.usages();
        String uri = "";
        RequestMapping classPathAnnotation = (RequestMapping) method.getDeclaringClass().getAnnotation(RequestMapping.class);
        if (dialogURI.length() > 0) {
            uri = resolvePathParameters(baseURI, (String) null, dialogURI, pathParameterValues);
        } else {
            uri = genericBaseURI + "/generic/generic" + dialogType + ".html";
            RequestMapping methodPathAnnotation = (RequestMapping) method.getAnnotation(RequestMapping.class);
            String parameter = resolvePathParameters(baseURI, pathAnnotationStringValue(classPathAnnotation), pathAnnotationStringValue(methodPathAnnotation), pathParameterValues);

            try {
                String encodedParameter = URLEncoder.encode(parameter, "UTF-8");
                uri = uri + "?" + parameterName + "=" + encodedParameter;
            } catch (UnsupportedEncodingException var27) {
                log.warn("Error encoding URI [{}]", parameter, var27);
            }

            StringBuilder resourceShapeParameters = new StringBuilder();
            if (resourceShapes != null) {
                String[] var20 = resourceShapes;
                int var21 = resourceShapes.length;

                for (int var22 = 0; var22 < var21; ++var22) {
                    String resourceShape = var20[var22];
                    String resourceShapeURI = baseURI + "/" + resourceShape;

                    try {
                        String encodedResourceShape = URLEncoder.encode(resourceShapeURI, "UTF-8");
                        resourceShapeParameters.append("&resourceShape=").append(encodedResourceShape);
                    } catch (UnsupportedEncodingException var26) {
                        log.warn("Error encoding URI [{}]", resourceShapeURI, var26);
                    }
                }
            }

            uri = uri + resourceShapeParameters.toString();
        }

        Dialog dialog = new Dialog(title, (new URI(uri)).normalize());
        if (label != null && label.length() > 0) {
            dialog.setLabel(label);
        }

        if (hintWidth != null && hintWidth.length() > 0) {
            dialog.setHintWidth(hintWidth);
        }

        if (hintHeight != null && hintHeight.length() > 0) {
            dialog.setHintHeight(hintHeight);
        }

        String[] var29 = resourceTypes;
        int var31 = resourceTypes.length;

        int var32;
        String usage;
        for (var32 = 0; var32 < var31; ++var32) {
            usage = var29[var32];
            dialog.addResourceType(new URI(usage));
        }

        var29 = usages;
        var31 = usages.length;

        for (var32 = 0; var32 < var31; ++var32) {
            usage = var29[var32];
            dialog.addUsage(new URI(usage));
        }

        return dialog;
    }

    private static String resolvePathParameters(String basePath, String classPathAnnotation, String methodPathAnnotation, Map<String, Object> pathParameterValues) {
        UriBuilder builder = UriBuilder.fromUri(basePath);
        if (classPathAnnotation != null && !classPathAnnotation.equals("")) {
            builder.path(classPathAnnotation);
        }

        if (methodPathAnnotation != null && !methodPathAnnotation.equals("")) {
            builder.path(methodPathAnnotation);
        }

        System.out.println("pathParameterValues: " + pathParameterValues);
        URI resolvedUri = builder.buildFromMap(pathParameterValues);
        if (resolvedUri != null) {
            return resolvedUri.toString();
        } else {
            log.warn("Could not build a path URI for basePath={} and path annotations (class='{}',method='{}')", new Object[]{basePath, classPathAnnotation, methodPathAnnotation});
            return null;
        }
    }
}