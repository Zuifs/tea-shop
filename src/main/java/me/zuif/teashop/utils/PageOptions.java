package me.zuif.teashop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
@AllArgsConstructor
public class PageOptions {
    private int page;
    private int size;

    public static PageOptions retrieveFromRequest(HttpServletRequest request) {
        int page = 0;
        int size = 16;
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        return new PageOptions(page, size);
    }
}
