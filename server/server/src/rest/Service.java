package rest;

import beans.ChangeOriginI;

public interface Service extends ChangeOriginI {

    default void before() {
    }

}
