package com.beta.cls.angelcar.api.model;

/**
 * Created by humnoy on 25/1/59.
 */
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
    };

    public enum FROM {
        USER {
            @Override
            public String toString() {
                return "user";
            }
        }, SHOP {
            @Override
            public String toString() {
                return "shop";
            }
        }
    }
}
