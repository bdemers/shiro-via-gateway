/*
 * Copyright 2019-Present Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.example.servlet;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple example servlet that displays the current user's details.
 */
@WebServlet(name = "UserProfile", urlPatterns = {"/", "/profile"})
public class UserProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // grab the current Shiro user
        Subject subject = SecurityUtils.getSubject();
        request.setAttribute("user", subject);

        // add some role checks
        request.setAttribute("role_Everyone", subject.hasRole("Everyone"));
        request.setAttribute("role_TestGroup", subject.hasRole("Test-Group"));
        request.setAttribute("role_Admin", subject.hasRole("Admin"));

        request.getRequestDispatcher("/WEB-INF/user-profile.jsp").forward(request, response);
    }
}