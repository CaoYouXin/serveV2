package blog.service.base;

import blog.service.exp.TableNotCreateException;
import blog.service.exp.TableNotSaveException;
import orm.Repository;
import rest.Service;

import java.util.List;

public abstract class BaseService<TABLE, ID> implements Service {

    protected abstract Repository<TABLE, ID> getRepository();

    protected abstract String getName();

    public List<TABLE> list() {
        return this.getRepository().findAll();
    }

    public TABLE save(TABLE data) {
        if (!this.getRepository().save(data)) {
            throw new TableNotSaveException(this.getName());
        }

        return data;
    }

    @Override
    public void before() {
        if (!this.getRepository().createTableIfNotExist()) {
            throw new TableNotCreateException(this.getName());
        }
    }
}
