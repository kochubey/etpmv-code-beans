package etpmv.system;

import etpmv.system.exception.UnsupportedMessageTextTypeException;

import javax.activation.DataHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Message {
    private Object message;

    public Message(Object message) {
        this.message = message;
    }

    public String toText() throws IOException, UnsupportedMessageTextTypeException {
        String text;
        if (message instanceof DataHandler) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            ((DataHandler) message).getInputStream(), "UTf-8")
            )) {
                text = br.lines().collect(joining(lineSeparator()));
            }
        } else if (message instanceof String) {
            text = (String) message;
        } else {
            throw new UnsupportedMessageTextTypeException(message);
        }
        return text;
    }
}
