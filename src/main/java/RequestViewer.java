import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestViewer {
    private final static String EQUAL_DELIMITER = " = ";
    private final static String COMMA_DELIMITER = ", ";
    private final static String SEMICOLON_DELIMITER = "; ";
    private final static List<String> history = new LinkedList<>();

    public void viewRequest(HttpServletRequest request) {
        StringBuilder response = new StringBuilder();

        response.append("Получен запрос типа ");
        response.append(getHttpMethod(request));
        response.append(" на адрес ");
        response.append(getRequestPath(request));
        response.append(" с заголовками: ");
        response.append(getHeaders(request));
        response.append(" параметрами: ");
        response.append(getParams(request));
        response.append(" и телом ");
        response.append(getBody(request));

        history.add(response.toString());

        for (String storedResponse : history) {
            System.out.println(storedResponse);
        }
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder outputHeaders = new StringBuilder();
        Enumeration<String> headers = request.getHeaderNames();

        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);

            outputHeaders.append(headerName);

            if (headerValues.hasMoreElements()) {
                outputHeaders.append(EQUAL_DELIMITER);

                while (headerValues.hasMoreElements()) {
                    outputHeaders.append(headerValues.nextElement());

                    if (headerValues.hasMoreElements()) {
                        outputHeaders.append(COMMA_DELIMITER);
                    }
                }

                outputHeaders.append(SEMICOLON_DELIMITER);
            }
        }

        return outputHeaders.toString();
    }

    private String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    private String getHttpMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    private String getParams(HttpServletRequest request) {
        StringBuilder outputParams = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();

            outputParams.append(name);
            outputParams.append(EQUAL_DELIMITER);
            outputParams.append(request.getParameter(name));

            if (parameterNames.hasMoreElements()) {
                outputParams.append(COMMA_DELIMITER);
            }
        }

        return outputParams.toString();
    }

    private String getBody(HttpServletRequest request) {
        StringBuilder outputBody = new StringBuilder();

        try {
            outputBody.append(request.getReader().lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {
            System.out.println("an error has occurred while parsing request body");
        }

        return outputBody.toString();
    }
}
