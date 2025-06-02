package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import java.lang.reflect.InvocationTargetException;

public class ClientCursoServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "ClientCursoServiceFactory.className";
    private static Class<ClientCursoService> serviceClass = null;

    private ClientCursoServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientCursoService> getServiceClass() {
        if (serviceClass == null) {
            try {
                String serviceClassName =
                        ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientCursoService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;
    }

    public static ClientCursoService getService() {
        try {
            return getServiceClass()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
