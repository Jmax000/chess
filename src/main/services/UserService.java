package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import requestResultObjects.*;

public class UserService
{
    /**
     * Register a new user.
     * @param request takes in a RegisterRequest obj
     * @return returns a RegisterResult obj
     */
    public RegisterResult register(RegisterRequest request)
    {
        try
        {
            if (UserDAO.find(request.getUsername()) != null)
            {
                RegisterResult invalidRegister = new RegisterResult();
                invalidRegister.setMessage("Error: already taken");
                return invalidRegister;
            }
            else if (request.getPassword() == null || request.getPassword().isEmpty())
            {
                RegisterResult invalidRegister = new RegisterResult();
                invalidRegister.setMessage("Error: bad request");
                return invalidRegister;
            }
            else //Need bad access??
            {
                UserDAO.createUser(request.getUsername(), request.getPassword(), request.getEmail());
                RegisterResult validRegister = new RegisterResult();
                validRegister.setUsername(request.getUsername());
                validRegister.setAuthToken(AuthDAO.findByName(request.getUsername()).getToken());
                return validRegister;
            }
        }
        catch (DataAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
