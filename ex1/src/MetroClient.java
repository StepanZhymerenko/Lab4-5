import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MetroClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            while (running) {
                System.out.println("Choose an option:");
                System.out.println("1. Issue a new card");
                System.out.println("2. Get client info");
                System.out.println("3. Top-up balance");
                System.out.println("4. Pay for trip");
                System.out.println("5. Check balance");
                System.out.println("6. Exit");

                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        issueNewCard(scanner);
                        break;
                    case 2:
                        getClientInfo(scanner);
                        break;
                    case 3:
                        topUpBalance(scanner);
                        break;
                    case 4:
                        payForTrip(scanner);
                        break;
                    case 5:
                        checkBalance(scanner);
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void issueNewCard(Scanner scanner) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Enter client ID:");
        String clientId = scanner.next();

        System.out.println("Enter initial balance:");
        String initialBalance = scanner.next();

        // Sending information about the new card to the server
        out.println("NewCard " + clientId + " " + initialBalance);

        // Receiving response from the server
        String response = in.readLine();
        System.out.println("Server response: " + response);

        socket.close();
    }

    private static void getClientInfo(Scanner scanner) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Enter client ID:");
        String clientId = scanner.next();

        // Getting information about the client
        out.println("GetInfo " + clientId);

        // Receiving response from the server
        String response = in.readLine();
        System.out.println("Server response: " + response);

        socket.close();
    }

    private static void topUpBalance(Scanner scanner) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Enter client ID:");
        String clientId = scanner.next();

        System.out.println("Enter amount to top up:");
        String amount = scanner.next();

        // Topping up the client's balance
        out.println("TopUp " + clientId + " " + amount);

        // Receiving response from the server
        String response = in.readLine();
        System.out.println("Server response: " + response);

        socket.close();
    }

    private static void payForTrip(Scanner scanner) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Enter client ID:");
        String clientId = scanner.next();

        System.out.println("Enter fare for the trip:");
        String fare = scanner.next();

        // Paying for the trip
        out.println("PayTrip " + clientId + " " + fare);

        // Receiving response from the server
        String response = in.readLine();
        System.out.println("Server response: " + response);

        socket.close();
    }

    private static void checkBalance(Scanner scanner) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Enter client ID:");
        String clientId = scanner.next();

        // Checking the client's balance
        out.println("GetBalance " + clientId);

        // Receiving response from the server
        String response = in.readLine();
        System.out.println("Server response: " + response);

        socket.close();
    }
}
