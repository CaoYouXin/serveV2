package meta.service;

import meta.data.EIService;
import meta.service.exp.ServiceSetException;
import rest.Service;

import java.util.List;

public interface IServiceService extends Service {

    void initAllService() throws ServiceSetException;

    List<EIService> listServices() throws ServiceSetException;

    EIService setService(String intfClassName, String implClassName) throws ServiceSetException;

    Boolean disableService(Long id, Boolean disabled) throws ServiceSetException;

}
