package meta.service;

import meta.data.EIController;
import meta.service.exp.ControllerSetException;
import rest.Service;

import java.util.List;

public interface IControllerService extends Service {

    List<EIController> listControllers();

    EIController setController(String className) throws ControllerSetException;

    Boolean setControllerDisabled(Long id, Boolean disabled) throws ControllerSetException;

}
