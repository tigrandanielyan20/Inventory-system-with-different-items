import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

//class Item with mian few things needed for item's deffinition
class Item {
    private String name;
    private String type;

    
    public Item(String name, String type) {
        this.name = name;
        this.type = type;
    }

    
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Item/name'" + name + "', type'" + type + "'";
    }
}

//The class for inventory
public class InventorySystem {
    private ArrayList<Item> inventory;
    private static final String[] TYPES = {"common", "great", "rare", "epic", "epic 1", "epic 2", "legendary"};

    //Constructor
    public InventorySystem() {
        inventory = new ArrayList<>();
    }

    //Add an item to inventory method
    public void addItem(String name, String type) {
        Item item = new Item(name, type);
        inventory.add(item);
        System.out.println("Item added: " + item);
    }

    //Display all items in inventory method
    public void displayInventory() {
        if (inventory.isEmpty()) {
            System.out.println("The inventory is empty.");
        } else {
            System.out.println("Current Inventory:");
            for (int i = 0; i < inventory.size(); i++) {
                System.out.println((i + 1) + ". " + inventory.get(i));
            }
        }
    }

    //Sort items by type in a defined order method
    public void sortInventory() {
        Collections.sort(inventory, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                int typeIndex1 = getTypeIndex(item1.getType());
                int typeIndex2 = getTypeIndex(item2.getType());
                return Integer.compare(typeIndex1, typeIndex2);
            }
        });
        System.out.println("Inventory sorted by type.");
    }

    //Get the index of a type in the TYPES array method
    private int getTypeIndex(String type) {
        for (int i = 0; i < TYPES.length; i++) {
            if (TYPES[i].equals(type)) {
                return i;
            }
        }
        return TYPES.length; 
    }

    //Combining method
    public void combineItems() {
        if (inventory.size() < 2) {
            System.out.println("Not enough items to combine.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        displayInventory();

        System.out.print("Select the base item by number: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Sorry, wrong input.");
            displayInventory();
            return;
        }

        int baseIndex = scanner.nextInt() - 1;

        if (baseIndex < 0 || baseIndex >= inventory.size()) {
            System.out.println("Sorry, wrong input.");
            displayInventory();
            return;
        }

        Item baseItem = inventory.get(baseIndex);
        String baseType = baseItem.getType();

        if (baseType.equals("epic 2")) {
            combineEpic2(baseItem, baseIndex);
        } else if (baseType.equals("epic")) {
            combineEpic(baseItem, baseIndex);
        } else {
            combineNormal(baseItem, baseIndex);
        }
    }
    
    //Under Epic combining method
    private void combineNormal(Item baseItem, int baseIndex) {
        ArrayList<Integer> matchingIndices = getMatchingItems(baseItem, baseIndex);

        if (matchingIndices.size() < 2) {
            System.out.println("Not enough matching items to combine.");
            return;
        }

        String baseType = baseItem.getType();
        String targetType = getNextType(baseType);

        if (targetType == null) {
            System.out.println("Cannot combine. Item is already at the highest type.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select two matching items by their numbers:");
        for (int index : matchingIndices) {
            System.out.println((index + 1) + ". " + inventory.get(index));
        }

        System.out.print("Enter the first item number: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Sorry, wrong input.");
            displayInventory();
            return;
        }

        int firstIndex = scanner.nextInt() - 1;

        System.out.print("Enter the second item number: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Sorry, wrong input.");
            displayInventory();
            return;
        }

        int secondIndex = scanner.nextInt() - 1;

        if (!matchingIndices.contains(firstIndex) || !matchingIndices.contains(secondIndex) || firstIndex == secondIndex) {
            System.out.println("Invalid selection.");
            return;
        }

        baseItem.setType(targetType);
        inventory.remove(Math.max(firstIndex, secondIndex));
        inventory.remove(Math.min(firstIndex, secondIndex));
        System.out.println("Items combined. New item: " + baseItem);
    }

    //Above Epic combining method
    private void combineEpic(Item baseItem, int baseIndex) {
        ArrayList<Integer> epicIndices = getMatchingItemsByType("epic", baseIndex);

        if (epicIndices.isEmpty()) {
            System.out.println("No other epic items available to combine.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select an epic item to combine with:");
        for (int index : epicIndices) {
            System.out.println((index + 1) + ". " + inventory.get(index));
        }

        System.out.print("Enter the item number: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Sorry, wrong input.");
            displayInventory();
            return;
        }

        int selectedIndex = scanner.nextInt() - 1;

        if (!epicIndices.contains(selectedIndex)) {
            System.out.println("Invalid selection.");
            return;
        }

        baseItem.setType("epic 1");
        inventory.remove(selectedIndex);
        System.out.println("Items combined. New item: " + baseItem);
    }

    //Legendary combining method
    private void combineEpic2(Item baseItem, int baseIndex) {
        ArrayList<Integer> epic2Indices = getMatchingItemsByType("epic 2", baseIndex);

        if (epic2Indices.size() < 2) {
            System.out.println("Not enough epic 2 items to combine.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select two epic 2 items to combine:");
        for (int index : epic2Indices) {
            System.out.println((index + 1) + ". " + inventory.get(index));
        }

        System.out.print("Enter the first item number: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Sorry, wrong input.");
            displayInventory();
            return;
        }

        int firstIndex = scanner.nextInt() - 1;

        System.out.print("Enter the second item number: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Sorry, wrong input.");
            displayInventory();
            return;
        }

        int secondIndex = scanner.nextInt() - 1;

        if (!epic2Indices.contains(firstIndex) || !epic2Indices.contains(secondIndex) || firstIndex == secondIndex) {
            System.out.println("Invalid selection.");
            return;
        }

        baseItem.setType("legendary");
        inventory.remove(Math.max(firstIndex, secondIndex));
        inventory.remove(Math.min(firstIndex, secondIndex));
        System.out.println("Items combined. New item: " + baseItem);
    }

    private ArrayList<Integer> getMatchingItems(Item baseItem, int baseIndex) {
        ArrayList<Integer> matchingIndices = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            if (i != baseIndex && inventory.get(i).getName().equals(baseItem.getName()) && inventory.get(i).getType().equals(baseItem.getType())) {
                matchingIndices.add(i);
            }
        }
        return matchingIndices;
    }

    private ArrayList<Integer> getMatchingItemsByType(String type, int baseIndex) {
        ArrayList<Integer> matchingIndices = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            if (i != baseIndex && inventory.get(i).getType().equals(type)) {
                matchingIndices.add(i);
            }
        }
        return matchingIndices;
    }

    private String getNextType(String currentType) {
        switch (currentType) {
            case "common":
                return "great";
            case "great":
                return "rare";
            case "rare":
                return "epic";
            case "epic 1":
                return "epic 2";
            default:
                return null;
        }
    }

    // Main method
    public static void main(String[] args) {
        InventorySystem inventorySystem = new InventorySystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nInventory System Menu:");
            System.out.println("1. Add Item with Type Selection");
            System.out.println("2. Display Inventory");
            System.out.println("3. Sort Inventory");
            System.out.println("4. Combine Items");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Sorry, wrong input.");
                inventorySystem.displayInventory();
                scanner.next(); 
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();

                    System.out.println("Select item type:");
                    for (int i = 0; i < TYPES.length; i++) {
                        System.out.println((i + 1) + ". " + TYPES[i]);
                    }
                    System.out.print("Enter your choice: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Sorry, wrong input.");
                        inventorySystem.displayInventory();
                        scanner.next(); 
                        break;
                    }

                    int typeChoice = scanner.nextInt();
                    scanner.nextLine(); 

                    if (typeChoice > 0 && typeChoice <= TYPES.length) {
                        String type = TYPES[typeChoice - 1];
                        inventorySystem.addItem(name, type);
                    } else {
                        System.out.println("Invalid type selection. Item not added.");
                    }
                    break;
                case 2:
                    inventorySystem.displayInventory();
                    break;
                case 3:
                    inventorySystem.sortInventory();
                    break;
                case 4:
                    inventorySystem.combineItems();
                    break;
                case 5:
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Sorry, wrong input.");
                    inventorySystem.displayInventory();
            }
        }
    }
}