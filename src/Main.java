import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    static ArrayList<Integer> ticketNumbers   = new ArrayList<>();
    static ArrayList<String>  requesterNames  = new ArrayList<>();
    static ArrayList<String>  issues          = new ArrayList<>();
    static ArrayList<String>  datesSubmitted  = new ArrayList<>();   
    static ArrayList<String>  statuses        = new ArrayList<>();   

    static Queue<Integer> supportQueue = new LinkedList<>();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            showMenu();
            choice = readInt("Enter your choice: ", 0, 12);
            System.out.println();

            switch (choice) {
                case 1:  createTicket();            break;
                case 2:  displayAllTickets("ALL TICKETS"); break;
                case 3:  searchTicket();            break;
                case 4:  enqueueTicket();           break;
                case 5:  dequeueTicket();           break;
                case 6:  peekQueue();               break;
                case 7:  displayQueue();            break;
                case 8:  checkQueueEmpty();         break;
                case 9:  bubbleSortByTicketNumber(); break;
                case 10: insertionSortByName();     break;
                case 11: selectionSortByDate();     break;
                case 12: loadSampleData();          break;
                case 0:  System.out.println("Thank you for using the IT Help Desk Ticketing System. Goodbye!"); break;
            }
        } while (choice != 0);
    }

    static void showMenu() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("        IT HELP DESK TICKETING SYSTEM");
        System.out.println("==============================================");
        System.out.println(" --- Ticket Records ---");
        System.out.println("  1. Create new ticket");
        System.out.println("  2. Display all tickets");
        System.out.println("  3. Search for a ticket");
        System.out.println(" --- Support Queue (FIFO) ---");
        System.out.println("  4. Enqueue a ticket (add to support queue)");
        System.out.println("  5. Dequeue / process next ticket");
        System.out.println("  6. Peek at next ticket");
        System.out.println("  7. Display support queue");
        System.out.println("  8. Check if queue is empty");
        System.out.println(" --- Sorting ---");
        System.out.println("  9. Bubble Sort    (by ticket number)");
        System.out.println(" 10. Insertion Sort (by requester name)");
        System.out.println(" 11. Selection Sort (by date submitted)");
        System.out.println(" --- Other ---");
        System.out.println(" 12. Load sample data");
        System.out.println("  0. Exit");
        System.out.println("==============================================");
    }

    static String readLine() {
        if (!scanner.hasNextLine()) {
            System.out.println("\nNo more input. Exiting program.");
            System.exit(0);
        }
        return scanner.nextLine();
    }

    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("  [Error] Please enter a number from " + min + " to " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [Error] Invalid input. Please enter a whole number.");
            }
        }
    }

    static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();
            if (input.isEmpty()) {
                System.out.println("  [Error] This field cannot be empty.");
            } else {
                return input;
            }
        }
    }

    static String readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();
            if (isValidDate(input)) {
                return input;
            }
            System.out.println("  [Error] Invalid date. Use the format YYYY-MM-DD (e.g., 2026-10-31).");
        }
    }

    static boolean isValidDate(String s) {
        if (!s.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }
        int year  = Integer.parseInt(s.substring(0, 4));
        int month = Integer.parseInt(s.substring(5, 7));
        int day   = Integer.parseInt(s.substring(8, 10));
        if (year < 2000 || month < 1 || month > 12 || day < 1) {
            return false;
        }
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean leap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
        int maxDay = daysInMonth[month - 1];
        if (month == 2 && leap) {
            maxDay = 29;
        }
        return day <= maxDay;
    }

    static int findIndexByTicket(int ticketNumber) {
        for (int i = 0; i < ticketNumbers.size(); i++) {
            if (ticketNumbers.get(i) == ticketNumber) {
                return i;
            }
        }
        return -1;
    }

    static void addRecord(int number, String name, String issue, String date, String status) {
        ticketNumbers.add(number);
        requesterNames.add(name);
        issues.add(issue);
        datesSubmitted.add(date);
        statuses.add(status);
    }

    static void swapRecords(int i, int j) {
        int    tNum    = ticketNumbers.get(i);
        String tName   = requesterNames.get(i);
        String tIssue  = issues.get(i);
        String tDate   = datesSubmitted.get(i);
        String tStatus = statuses.get(i);

        setRecord(i, ticketNumbers.get(j), requesterNames.get(j), issues.get(j),
                datesSubmitted.get(j), statuses.get(j));
        setRecord(j, tNum, tName, tIssue, tDate, tStatus);
    }

    static void setRecord(int i, int number, String name, String issue, String date, String status) {
        ticketNumbers.set(i, number);
        requesterNames.set(i, name);
        issues.set(i, issue);
        datesSubmitted.set(i, date);
        statuses.set(i, status);
    }

    static void copyRecord(int from, int to) {
        setRecord(to, ticketNumbers.get(from), requesterNames.get(from), issues.get(from),
                datesSubmitted.get(from), statuses.get(from));
    }

    static void printTableHeader() {
        System.out.printf("%-4s %-9s %-18s %-28s %-12s %-10s%n",
                "No.", "Ticket #", "Requester", "Issue", "Date", "Status");
        System.out.println("------------------------------------------------------------------------------------");
    }

    static void printRecordRow(int position, int index) {
        System.out.printf("%-4d %-9d %-18s %-28s %-12s %-10s%n",
                position,
                ticketNumbers.get(index),
                requesterNames.get(index),
                issues.get(index),
                datesSubmitted.get(index),
                statuses.get(index));
    }

    static void createTicket() {
        System.out.println("--- CREATE NEW TICKET ---");
        int number;
        while (true) {
            number = readInt("Ticket number (1-999999): ", 1, 999999);
            if (findIndexByTicket(number) != -1) {
                System.out.println("  [Error] Ticket #" + number + " already exists. Use a different number.");
            } else {
                break;
            }
        }
        String name  = readNonEmpty("Requester name: ");
        String issue = readNonEmpty("Issue description: ");
        String date  = readDate("Date submitted (YYYY-MM-DD): ");

        addRecord(number, name, issue, date, "Pending");
        System.out.println("Ticket #" + number + " created successfully (Status: Pending).");
        System.out.println("Tip: use option 4 to add it to the support queue.");
        System.out.println();
        displayAllTickets("UPDATED TICKET LIST");
    }

    static void displayAllTickets(String title) {
        System.out.println("--- " + title + " ---");
        if (ticketNumbers.isEmpty()) {
            System.out.println("No tickets found. Create a ticket first (option 1) or load sample data (option 12).");
            return;
        }
        printTableHeader();
        for (int i = 0; i < ticketNumbers.size(); i++) {
            printRecordRow(i + 1, i);
        }
        System.out.println("Total tickets: " + ticketNumbers.size());
    }

    static void searchTicket() {
        System.out.println("--- SEARCH TICKET ---");
        if (ticketNumbers.isEmpty()) {
            System.out.println("There are no tickets to search.");
            return;
        }
        System.out.println("  1. Search by ticket number");
        System.out.println("  2. Search by requester name");
        int option = readInt("Choose search type: ", 1, 2);

        if (option == 1) {
            int number = readInt("Enter ticket number: ", 1, 999999);
            int index = findIndexByTicket(number);   // linear search
            if (index == -1) {
                System.out.println("Ticket #" + number + " was not found.");
            } else {
                System.out.println("Ticket found:");
                printTableHeader();
                printRecordRow(1, index);
            }
        } else {
            String keyword = readNonEmpty("Enter requester name (or part of it): ").toLowerCase();
            int found = 0;
            for (int i = 0; i < requesterNames.size(); i++) {
                if (requesterNames.get(i).toLowerCase().contains(keyword)) {
                    if (found == 0) {
                        System.out.println("Matching tickets:");
                        printTableHeader();
                    }
                    found++;
                    printRecordRow(found, i);
                }
            }
            if (found == 0) {
                System.out.println("No ticket found for requester \"" + keyword + "\".");
            }
        }
    }


    static void enqueueTicket() {
        System.out.println("--- ENQUEUE TICKET ---");
        if (ticketNumbers.isEmpty()) {
            System.out.println("There are no tickets yet. Create a ticket first.");
            return;
        }
        int number = readInt("Enter ticket number to add to the queue: ", 1, 999999);
        int index = findIndexByTicket(number);

        if (index == -1) {
            System.out.println("Ticket #" + number + " does not exist.");
        } else if (statuses.get(index).equals("In Queue")) {
            System.out.println("Ticket #" + number + " is already in the queue.");
        } else if (statuses.get(index).equals("Processed")) {
            System.out.println("Ticket #" + number + " has already been processed.");
        } else {
            supportQueue.add(number);
            statuses.set(index, "In Queue");
            System.out.println("Ticket #" + number + " (" + requesterNames.get(index)
                    + ") added to the back of the queue. Position: " + supportQueue.size());
        }
    }

    static void dequeueTicket() {
        System.out.println("--- PROCESS NEXT TICKET (DEQUEUE) ---");
        if (supportQueue.isEmpty()) {
            System.out.println("The queue is empty. There is no ticket to process.");
            return;
        }
        int number = supportQueue.poll();
        int index = findIndexByTicket(number);
        statuses.set(index, "Processed");
        System.out.println("Now processing ticket #" + number);
        System.out.println("  Requester : " + requesterNames.get(index));
        System.out.println("  Issue     : " + issues.get(index));
        System.out.println("  Submitted : " + datesSubmitted.get(index));
        System.out.println("Tickets still waiting: " + supportQueue.size());
    }

    static void peekQueue() {
        System.out.println("--- PEEK NEXT TICKET ---");
        if (supportQueue.isEmpty()) {
            System.out.println("The queue is empty. Nothing to peek at.");
            return;
        }
        int number = supportQueue.peek();
        int index = findIndexByTicket(number);
        System.out.println("Next ticket to be processed (not removed from queue):");
        printTableHeader();
        printRecordRow(1, index);
    }

    static void displayQueue() {
        System.out.println("--- SUPPORT QUEUE ---");
        if (supportQueue.isEmpty()) {
            System.out.println("The queue is empty.");
            return;
        }
        System.out.println("FRONT (processed first)  -->  REAR (processed last)");
        printTableHeader();
        int position = 1;
        for (int number : supportQueue) {
            printRecordRow(position, findIndexByTicket(number));
            position++;
        }
        System.out.println("Tickets in queue: " + supportQueue.size());
    }

    static void checkQueueEmpty() {
        System.out.println("--- CHECK QUEUE ---");
        if (supportQueue.isEmpty()) {
            System.out.println("Yes, the queue is EMPTY.");
        } else {
            System.out.println("No, the queue is NOT empty. It has " + supportQueue.size() + " ticket(s).");
        }
    }


    static boolean canSort() {
        if (ticketNumbers.size() < 2) {
            System.out.println("You need at least 2 tickets to sort. Create more tickets or load sample data (option 12).");
            return false;
        }
        return true;
    }

    static boolean askAscending() {
        System.out.println("  1. Ascending");
        System.out.println("  2. Descending");
        return readInt("Sort order: ", 1, 2) == 1;
    }

    static void bubbleSortByTicketNumber() {
        System.out.println("--- BUBBLE SORT BY TICKET NUMBER ---");
        if (!canSort()) return;
        boolean ascending = askAscending();

        displayAllTickets("BEFORE SORTING");

        int n = ticketNumbers.size();
        for (int pass = 0; pass < n - 1; pass++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - pass; j++) {
                int a = ticketNumbers.get(j);
                int b = ticketNumbers.get(j + 1);
                boolean outOfOrder = ascending ? (a > b) : (a < b);
                if (outOfOrder) {
                    swapRecords(j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;   
        }

        System.out.println();
        displayAllTickets("AFTER BUBBLE SORT (" + (ascending ? "ascending" : "descending") + ")");
    }

    static void insertionSortByName() {
        System.out.println("--- INSERTION SORT BY REQUESTER NAME ---");
        if (!canSort()) return;
        boolean ascending = askAscending();

        displayAllTickets("BEFORE SORTING");

        int n = requesterNames.size();
        for (int i = 1; i < n; i++) {
            // hold the record to insert
            int    keyNum    = ticketNumbers.get(i);
            String keyName   = requesterNames.get(i);
            String keyIssue  = issues.get(i);
            String keyDate   = datesSubmitted.get(i);
            String keyStatus = statuses.get(i);

            int j = i - 1;
            while (j >= 0) {
                int cmp = requesterNames.get(j).compareToIgnoreCase(keyName);
                boolean shouldShift = ascending ? (cmp > 0) : (cmp < 0);
                if (!shouldShift) break;
                copyRecord(j, j + 1);   
                j--;
            }
            setRecord(j + 1, keyNum, keyName, keyIssue, keyDate, keyStatus);
        }

        System.out.println();
        displayAllTickets("AFTER INSERTION SORT (" + (ascending ? "A-Z" : "Z-A") + ")");
    }

    static void selectionSortByDate() {
        System.out.println("--- SELECTION SORT BY DATE SUBMITTED ---");
        if (!canSort()) return;
        boolean ascending = askAscending();

        displayAllTickets("BEFORE SORTING");

        int n = datesSubmitted.size();
        for (int i = 0; i < n - 1; i++) {
            int best = i;
            for (int j = i + 1; j < n; j++) {
                int cmp = datesSubmitted.get(j).compareTo(datesSubmitted.get(best));
                boolean better = ascending ? (cmp < 0) : (cmp > 0);
                if (better) {
                    best = j;
                }
            }
            if (best != i) {
                swapRecords(i, best);
            }
        }

        System.out.println();
        displayAllTickets("AFTER SELECTION SORT (" + (ascending ? "oldest first" : "newest first") + ")");
    }

    static void loadSampleData() {
        System.out.println("--- LOAD SAMPLE DATA ---");
        int[]    nums    = {1005, 1002, 1008, 1001, 1004, 1007};
        String[] names   = {"Maria Santos", "Juan Dela Cruz", "Ana Reyes", "Carlo Mendoza", "Bea Villanueva", "Ramon Cruz"};
        String[] probs   = {"Cannot connect to wifi", "Forgot account password", "Printer not working",
                            "Laptop will not boot", "Email not syncing", "Projector has no display"};
        String[] dates   = {"2026-10-20", "2026-10-18", "2026-10-25", "2026-10-15", "2026-10-22", "2026-10-28"};

        int added = 0;
        for (int i = 0; i < nums.length; i++) {
            if (findIndexByTicket(nums[i]) == -1) {   
                addRecord(nums[i], names[i], probs[i], dates[i], "Pending");
                added++;
            }
        }
        if (added == 0) {
            System.out.println("Sample data is already loaded.");
        } else {
            System.out.println(added + " sample ticket(s) loaded.");
        }
    }
}
