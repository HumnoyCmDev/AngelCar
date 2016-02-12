package com.hndev.library.util;

/**
 * Created by humnoy on 25/1/59.
 */

@Deprecated
public enum TypeChat {
    WAIT {
        @Override
        public String toString() {
            return "wait";
        }
    }, VIEW {
        @Override
        public String toString() {
            return "view";
        }
    }
}
