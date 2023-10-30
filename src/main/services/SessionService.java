package services;

import dataAccess.*;
import models.*;
import requestResultObjects.*;

public class SessionService
{
    /**
     * Logs in an existing user (returns a new authToken).
     * @param request takes in a LoginRequest obj
     * @return returns a LoginResult obj
     */
    public LoginResult login(LoginRequest request)
    {
        try
        {
            User user = UserDAO.find(request.getUsername());
            if (user == null)
            {
                LoginResult invalidLogin = new LoginResult();
                invalidLogin.setMessage("Error: unauthorized");
                return invalidLogin;
            }
            else if (!(user.getPassword().equals(request.getPassword())))
            {
                LoginResult invalidLogin = new LoginResult();
                invalidLogin.setMessage("Error: unauthorized");
                return invalidLogin;
            }
            else
            {
                Authtoken authtoken = AuthDAO.setNewAuthtoken(user.getUsername());
                LoginResult validLogin = new LoginResult();
                validLogin.setUsername(user.getUsername());
                validLogin.setAuthToken(authtoken.getToken());
                return validLogin;
            }
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * Logs out the user represented by the authToken.
     * @param token takes in a authtoken string
     * @return returns a LoginResult obj
     */
    public LogoutResult logout(String token)
    {
        try
        {
            Authtoken authtoken = AuthDAO.findByToken(token);
            if (authtoken == null)
            {
                LogoutResult invalidLogout = new LogoutResult();
                invalidLogout.setMessage("Error: unauthorized");
                return invalidLogout;
            }
            else
            {
                AuthDAO.remove(authtoken.getUsername());
                LogoutResult validLogout = new LogoutResult();
                validLogout.setAuthToken(token);
                return validLogout;
            }
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
