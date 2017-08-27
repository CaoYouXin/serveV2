package meta.service;

import meta.data.EIAuth;
import rest.Service;

import java.util.List;

public interface IAuthService extends Service {

    Boolean setAuths(List<EIAuth> authList);

    void initAuths();

}
