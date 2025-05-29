package es.udc.ws.app.model.curso;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlCursoDaoFactory {
    private final static String CLASS_NAME_PARAMETER = "SqlCursoDaoFactory.className";
    private static SqlCursoDao dao = null;

    private SqlCursoDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlCursoDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlCursoDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlCursoDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }

}
