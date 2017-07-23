package meta.data;

import beans.EntityBeanI;

import javax.persistence.*;

@Entity(name = "interceptor")
public interface EIInterceptor extends EntityBeanI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long getInterceptorId();

    void setInterceptorId(Long interceptorId);

    @Column(name = "name", nullable = false)
    String getInterceptorName();

    void setInterceptorName(String name);

    @Column(name = "is_post", nullable = false)
    Boolean isPostInterceptor();

    void setPostInterceptor(Boolean isPostInterceptor);

    @Column(name = "className", nullable = false)
    String getInterceptorClassName();

    void setInterceptorClassName(String className);

    @Column(name = "disabled", nullable = false)
    Boolean isInterceptorDisabled();

    void setInterceptorDisabled(Boolean disabled);

}
