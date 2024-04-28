import java.io.*;
import java.net.*;
import java.util.*;

public class MetroServer {
    private static final int PORT = 12345;
    private static final Map<String, Double> cardDatabase = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String request;
                while ((request = in.readLine()) != null) {
                    String[] tokens = request.split(" ");
                    String command = tokens[0];

                    switch (command) {
                        case "NewCard":
                            handleNewCard(tokens);
                            break;
                        case "GetInfo":
                            handleGetInfo(tokens);
                            break;
                        case "TopUp":
                            handleTopUp(tokens);
                            break;
                        case "PayTrip":
                            handlePayTrip(tokens);
                            break;
                        case "GetBalance":
                            handleGetBalance(tokens);
                            break;
                        default:
                            out.println("Invalid command");
                            break;
                    }
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleNewCard(String[] tokens) {
            if (tokens.length != 3) {
                out.println("Usage: NewCard <client_id> <initial_balance>");
                return;
            }

            String clientId = tokens[1];
            double initialBalance;
            try {
                initialBalance = Double.parseDouble(tokens[2]);
            } catch (NumberFormatException e) {
                out.println("Invalid initial balance");
                return;
            }

            if (cardDatabase.containsKey(clientId)) {
                out.println("Client already has a card");
            } else {
                cardDatabase.put(clientId, initialBalance);
                out.println("New card issued and registered for client " + clientId);
            }
        }

        private void handleGetInfo(String[] tokens) {
            if (tokens.length != 2) {
                out.println("Usage: GetInfo <client_id>");
                return;
            }

            String clientId = tokens[1];
            if (cardDatabase.containsKey(clientId)) {
                double balance = cardDatabase.get(clientId);
                out.println("Client ID: " + clientId + ", Balance: " + balance);
            } else {
                out.println("Client not found");
            }
        }

        private void handleTopUp(String[] tokens) {
            if (tokens.length != 3) {
                out.println("Usage: TopUp <client_id> <amount>");
                return;
            }

            String clientId = tokens[1];
            double amount;
            try {
                amount = Double.parseDouble(tokens[2]);
            } catch (NumberFormatException e) {
                out.println("Invalid amount");
                return;
            }

            if (cardDatabase.containsKey(clientId)) {
                double balance = cardDatabase.get(clientId);
                balance += amount;
                cardDatabase.put(clientId, balance);
                out.println("Balance topped up. New balance for client " + clientId + ": " + balance);
            } else {
                out.println("Client not found");
            }
        }

        private void handlePayTrip(String[] tokens) {
            if (tokens.length != 3) {
                out.println("Usage: PayTrip <client_id> <fare>");
                return;
            }

            String clientId = tokens[1];
            double fare;
            try {
                fare = Double.parseDouble(tokens[2]);
            } catch (NumberFormatException e) {
                out.println("Invalid fare");
                return;
            }

            if (cardDatabase.containsKey(clientId)) {
                double balance = cardDatabase.get(clientId);
                if (balance >= fare) {
                    balance -= fare;
                    cardDatabase.put(clientId, balance);
                    out.println("Trip paid. New balance for client " + clientId + ": " + balance);
                } else {
                    out.println("Insufficient balance");
                }
            } else {
                out.println("Client not found");
            }
        }

        private void handleGetBalance(String[] tokens) {
            if (tokens.length != 2) {
                out.println("Usage: GetBalance <client_id>");
                return;
            }

            String clientId = tokens[1];
            if (cardDatabase.containsKey(clientId)) {
                double balance = cardDatabase.get(clientId);
                out.println("Balance for client " + clientId + ": " + balance);
            } else {
                out.println("Client not found");
            }
        }
    }
}
