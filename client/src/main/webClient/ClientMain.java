package webClient;


public class ClientMain {
    public static void main(String[] args) throws Exception
    {
        var serverUrl = "http://localhost:8082";
        if (args.length == 1)
        {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();
    }
}