package io.intercom.retrofit;

import io.intercom.retrofit.RequestInterceptor;
import java.util.ArrayList;
import java.util.List;

final class RequestInterceptorTape implements RequestInterceptor.RequestFacade, RequestInterceptor {
    private final List<CommandWithParams> tape = new ArrayList();

    private enum Command {
        ADD_HEADER {
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addHeader(name, value);
            }
        },
        ADD_PATH_PARAM {
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addPathParam(name, value);
            }
        },
        ADD_ENCODED_PATH_PARAM {
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addEncodedPathParam(name, value);
            }
        },
        ADD_QUERY_PARAM {
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addQueryParam(name, value);
            }
        },
        ADD_ENCODED_QUERY_PARAM {
            public void intercept(RequestInterceptor.RequestFacade facade, String name, String value) {
                facade.addEncodedQueryParam(name, value);
            }
        };

        /* access modifiers changed from: package-private */
        public abstract void intercept(RequestInterceptor.RequestFacade requestFacade, String str, String str2);
    }

    RequestInterceptorTape() {
    }

    public void addHeader(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_HEADER, name, value));
    }

    public void addPathParam(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_PATH_PARAM, name, value));
    }

    public void addEncodedPathParam(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_ENCODED_PATH_PARAM, name, value));
    }

    public void addQueryParam(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_QUERY_PARAM, name, value));
    }

    public void addEncodedQueryParam(String name, String value) {
        this.tape.add(new CommandWithParams(Command.ADD_ENCODED_QUERY_PARAM, name, value));
    }

    public void intercept(RequestInterceptor.RequestFacade request) {
        for (CommandWithParams cwp : this.tape) {
            cwp.command.intercept(request, cwp.name, cwp.value);
        }
    }

    private static final class CommandWithParams {
        final Command command;
        final String name;
        final String value;

        CommandWithParams(Command command2, String name2, String value2) {
            this.command = command2;
            this.name = name2;
            this.value = value2;
        }
    }
}
