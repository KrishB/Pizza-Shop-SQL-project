import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

	private Connection connection;
	private Statement statement;
	private Statement statement2;
	private ResultSet resultSet;
	private ResultSet resultSet2;

	public PreparedStatement p1;

	
	public String user_first_name;
	public String user_last_name;
	public int user_id;
	public int balance;
	public static int order_num = 1;
	
	Scanner scan = new Scanner(System.in);
		
	public main(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root","admin");
			statement = connection.createStatement();
			statement2 = connection.createStatement();
		}catch(Exception e){
			System.out.println("error: " + e);
		}
		
		System.out.println("Welcome to the pizza shop!  What would you like to do: ");
		System.out.println("");
		System.out.println("1. Register as user");
		System.out.println("2. Update my information");
		System.out.println("3. Make an Order");
		System.out.println("4. Put a hold on a standing order");
		System.out.println("5. Information on our products");
		System.out.println("6. Information on our current products on sale (admin only)");
		System.out.println("7. See your current orders and expected delivery time");
		System.out.println("8. See your current balance");
		System.out.println("9. See your transactions from any time period");
		System.out.println("10. Suggest me a product");
		System.out.println("11. Make a payment");
		System.out.println("12. See the reports (admin only)");
		System.out.println("13. Reactivate an account (admin only)");
		System.out.println("14. Fulfill an order");
		
		int input = scan.nextInt();
		scan.nextLine();
		if(input != 1){
			System.out.println("What is your user id?");
			user_id = scan.nextInt();
			scan.nextLine();
			System.out.println();
		}
		String first_name = null;
		String last_name = null;
		try{
			String query = ("SELECT balance, first_name, last_name FROM customers WHERE customer_id = ?;");

			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				first_name = resultSet.getString("first_name");
				last_name = resultSet.getString("last_name");
				balance = resultSet.getInt("balance");
			}
		} catch (Exception e){
			System.out.println("error: " + e);
		}
		if(balance < 0){
			deactivateCustomer();
			System.out.println("You have a balance of " + balance + " , therefore your account is deactivated.");
		} else {
			if(input != 1){
				System.out.println("Welcome " + first_name + " " + last_name + "!");
			}
			switch(input){
				case 1: customerSetUp();
						break;
				case 2: askForUpdate();
						break;
				case 3: orderPlacement();
						break;
				
				case 4: putOnHold();
						break;
				
				case 5: productQuery();
						break;
				case 6: itemOnSale();
						break;
				
				case 7: orderQuery();
						break;
					
				case 8: balanceQuery();
						break;
				case 9: transactionHistory();
						break;
				case 10: suggestProduct();
						break;
				case 11: receivePayment();
						break;
				case 12: System.out.println("Which report would you like to see?");
				   		System.out.println("1. Best Selling report");
				   		System.out.println("2. Worst selling report");
				   		System.out.println("3. Delinquency report");
				   		input = scan.nextInt();
				   		scan.nextLine();
				   		if(input == 1){
				   			bestSellerReport();
				   		} else if(input == 2){
				   			worstSellerReport();
				   		} else {
				   			delinquencyReport();
				   		}
				   		break;
				case 13: reactivateCustomer();
						break;
				case 14: displayCurrentOrders();
						break;
			}
		}
	}
	
	public void getData(){
		try{
			String query = "select * from customers";
			resultSet = statement.executeQuery(query);
			System.out.println("records from databases");
			while(resultSet.next()){
				String name = resultSet.getString("first_name");
				String active = resultSet.getString("active");
				System.out.println("Name " + name + " active " + active);
			}
		}catch(Exception e){
			System.out.println("error: " + e);
		}
	}
	
	private void customerSetUp(){
		
		System.out.println("Please enter your first name: ");
		String first_name = scan.nextLine();
		System.out.println("Please enter your last name: ");
		String last_name = scan.nextLine();
		System.out.println("Please enter your address(Postal code): ");
		String address = scan.nextLine();
		System.out.println("Please enter your sex(M/F): ");
		String sex = scan.nextLine();
		System.out.println("Please enter your age: ");
		int age = scan.nextInt();
		scan.nextLine();
		System.out.println("Please enter your method of payment(cash/credit card/check): ");
		String method_of_payment = scan.nextLine();
		System.out.println("Please enter your current balance: ");
		int balance = scan.nextInt();
		scan.nextLine();
		
		if("Cash".equalsIgnoreCase(method_of_payment)){ // payment by cash
			try {
				String query = " insert into customers(first_name, last_name, address, sex, age, method_of_payment, active, balance)" + "values (?, ?, ?, ?, ?, ?, ?, ?)";
				p1 = connection.prepareStatement(query);
				
				p1.setString(1, first_name);
				p1.setString(2, last_name);
				p1.setString(3, address);
				p1.setString(4, sex);
				p1.setInt(5, age);
				p1.setString(6, method_of_payment);
				p1.setInt(7, 1);
				p1.setInt(8, balance);
				p1.execute();
			} catch(Exception e) {
				System.out.println("error: " + e);
			}
		} else if("Credit card".equalsIgnoreCase(method_of_payment) ) { // payment by credit card
			System.out.println("Please enter your credit card number: ");
			Long credit_card_number = scan.nextLong();
			scan.nextLine();
			
			try {
				String query = " insert into customers(first_name, last_name, address, sex, age, method_of_payment,credit_card_number, active, balance)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				p1 = connection.prepareStatement(query);
				
				p1.setString(1, first_name);
				p1.setString(2, last_name);
				p1.setString(3, address);
				p1.setString(4, sex);
				p1.setInt(5, age);
				p1.setString(6, "Credit Card");
				p1.setLong(7, credit_card_number);
				p1.setInt(8, 1);
				p1.setInt(9, balance);
				p1.execute();
			} catch(Exception e) {
				System.out.println("error: " + e);
			}
		} else {
			System.out.println("Please enter your balance limit: ");
			int balance_limit = scan.nextInt();
			scan.nextLine();
			try {
				String query = " insert into customers(first_name, last_name, address, sex, age, method_of_payment, balance_limit, active, balance)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				p1 = connection.prepareStatement(query);
				
				p1.setString(1, first_name);
				p1.setString(2, last_name);
				p1.setString(3, address);
				p1.setString(4, sex);
				p1.setInt(5, age);
				p1.setString(6, "Check");
				p1.setInt(7, balance_limit);
				p1.setInt(8, 1);
				p1.setInt(9, balance);
				p1.execute();
			} catch(Exception e) {
				System.out.println("error: " + e);
			}
		}
	}
	
	private void askForUpdate(){
		
		int id = 0;
		String method_of_payment = null;
		try{
			String query = ("SELECT customer_id, method_of_payment FROM customers WHERE first_name = ? AND last_name = ?;");
			p1 = connection.prepareStatement(query);
			p1.setString(1, user_first_name);
			p1.setString(2, user_last_name);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				id = resultSet.getInt("customer_id");
				method_of_payment = resultSet.getString("method_of_payment");
			} 
		}catch(Exception e){
			System.out.println("error: " + e);
		}
		
		System.out.println("Which value would you like to update?");
		System.out.println("1. first name");
		System.out.println("2. Last name");
		System.out.println("3. address");
		System.out.println("4. sex");
		System.out.println("5. age");
		System.out.println("6. method of payment");
		if(method_of_payment.equalsIgnoreCase("Credit Card")){
			System.out.println("7. credit card number");
		} else if(method_of_payment.equalsIgnoreCase("check")){
			System.out.println("7. balance limit");
		}
		System.out.println("8. balance");
		
		int num = scan.nextInt();
		scan.nextLine();
		
		System.out.println("What new value do you want?");
		
		String update1;
		int update2;
		long update3;
		
		switch(num){
			case 1: update1 = scan.nextLine();
					updateCustomer(id, num, update1);
					break;
			case 2: update1 = scan.nextLine();
					updateCustomer(id, num, update1);
					break;
			case 3: update1 = scan.nextLine();
					updateCustomer(id, num, update1);
					break;
			case 4:	update1 = scan.nextLine();
				   	updateCustomer(id, num, update1);
					break;
			case 5:	update2 = scan.nextInt();
					updateCustomer(id, num, update2);
					break;
			case 6:	update1 = scan.nextLine();
					updateCustomer(id, num, update1);
					break;
			case 7:	if(method_of_payment.equalsIgnoreCase("Credit card")){
						update3 = scan.nextLong();
						updateCustomer(id, update3);
					} else {
						update2 = scan.nextInt();
			  			updateCustomer(id, num, update2);
					}
		  			break;
			case 8:	update2 = scan.nextInt();
				  	updateCustomer(id, num, update2);
				  	break;
		}
	}
	
	private void updateCustomer(int id, int attribute, String value){ // for updating string values
		
		String query = null;
		switch(attribute){
			case 1: query = "UPDATE customers SET first_name = ? WHERE customer_id = ?";
					try {
						p1 = connection.prepareStatement(query);
						p1.setString(1, value);
						p1.setInt(2, id);
						p1.executeUpdate();
					} catch (SQLException e) {
						System.out.println("error: " + e);
					}
					break;
			case 2:	query = "UPDATE customers SET last_name = ? WHERE customer_id = ?";
					try {
						p1 = connection.prepareStatement(query);
						p1.setString(1, value);
						p1.setInt(2, id);
						p1.executeUpdate();
					} catch (SQLException e) {
						System.out.println("error: " + e);
					}
					break;
			case 3:	query = "UPDATE customers SET address = ? WHERE customer_id = ?";
					try {
						p1 = connection.prepareStatement(query);
						p1.setString(1, value);
						p1.setInt(2, id);
						p1.executeUpdate();
					} catch (SQLException e) {
						System.out.println("error: " + e);
					}
					break;
			case 4:	query = "UPDATE customers SET sex = ? WHERE customer_id = ?";
					try {
						p1 = connection.prepareStatement(query);
						p1.setString(1, value);
						p1.setInt(2, id);
						p1.executeUpdate();
					} catch (SQLException e) {
						System.out.println("error: " + e);
					}
					break;
			case 6:	query = "UPDATE customers SET method_of_payment = ? WHERE customer_id = ?";
					try {
						p1 = connection.prepareStatement(query);
						p1.setString(1, value);
						p1.setInt(2, id);
						p1.executeUpdate();
					} catch (SQLException e) {
						System.out.println("error: " + e);
					}
					break;
		}
		
	}
	
	private void updateCustomer(int id, int attribute, int value){ // for updating int values
	
		String query = null;
		switch(attribute){
			case 5: query = "UPDATE customers SET age = ? WHERE customer_id = ?";
					try {
						p1 = connection.prepareStatement(query);
						p1.setInt(1, value);
						p1.setInt(2, id);
						p1.executeUpdate();
					} catch (SQLException e) {
						System.out.println("error: " + e);
					}
					break;
			case 7:	query = "UPDATE customers SET balance_limit = ? WHERE customer_id = ?";
					try {
						p1 = connection.prepareStatement(query);
						p1.setInt(1, value);
						p1.setInt(2, id);
						p1.executeUpdate();
					} catch (SQLException e) {
						System.out.println("error: " + e);
					}
					break;
			case 8: query = "UPDATE customers SET balance = ? WHERE customer_id = ?";
					try {
						p1 = connection.prepareStatement(query);
						p1.setInt(1, value);
						p1.setInt(2, id);
						p1.executeUpdate();
					} catch (SQLException e) {
						System.out.println("error: " + e);
					}
					break;
		}
	}
	
	private void updateCustomer(int id, long value){ // for updating credit card numbers
		String query = "UPDATE customers SET credit_card_number = ? WHERE customer_id = ?";
		try {
			p1 = connection.prepareStatement(query);
			p1.setLong(1, value);
			p1.setInt(2, id);
			p1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}
	
	private void deactivateCustomer(){
		
		try{
			String query = "UPDATE customers SET active = ? WHERE customer_id = ?";
			p1 = connection.prepareStatement(query);
			p1.setInt(1, 0);
			p1.setInt(2, user_id);
			p1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}
	
	private void reactivateCustomer(){
		try{
			String query = "UPDATE customers SET active = ? WHERE customer_id = ?";
			p1 = connection.prepareStatement(query);
			p1.setInt(1, 1);
			p1.setInt(2, user_id);
			p1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}
	
	private void orderPlacement(){
		
		ArrayList<String> order_list = new ArrayList<String>();
		boolean done = false;
		int done_num = 0;
		float price = 0;
		int taxable = 0;
		while(!done){
			if(done_num == 0){
				System.out.println("What would you like to order?");
			} else {
				System.out.println("Currently ordered: ");
				for(int i = 0; i < order_list.size(); i++){
					System.out.println(order_list.get(i).toString());
				}
				System.out.println();
				System.out.println("What else would you like to order?");
			}
			System.out.println("1. Pizza");
			System.out.println("2. Drink");
			System.out.println("3. Side Dishes");
			System.out.println("4. On Sale");
			
			int order = scan.nextInt();
			scan.nextLine();
			int sub_order = 0;
			try{
				if(order == 1){
					System.out.println("What size would you like?");
					System.out.println("1. Small");
					System.out.println("2. Medium");
					System.out.println("3. Large");
					System.out.println("4. X-Large");
					int size = scan.nextInt();
					String size_pizza = null;
					float price_pizza = 0;
					switch(size){
						case 1: size_pizza = "Small";
								price_pizza = 0.75f;
								break;
						case 2: size_pizza = "Medium";
								price_pizza = 1;
								break;
						case 3: size_pizza = "Large";
								price_pizza = 1.25f;
								break;
						case 4: size_pizza = "X-Large";
								price_pizza = 1.5f;
								break;
					}
					
					System.out.println("What toppings would you like?");
					String query = "select product_id, description, price, taxable from products where category = ' Pizza' AND sale = 0";
					resultSet = statement.executeQuery(query);
					int product_id = 0;
					String description = null;
					while(resultSet.next()){
						taxable = resultSet.getInt("taxable");
						product_id = resultSet.getInt("product_id");
						description = resultSet.getString("description");
						if((taxable = resultSet.getInt("taxable")) == 1){
							price = resultSet.getFloat("price");
							price *= price_pizza;
							price = price + (price * 0.13F); 
						} else {
							price = resultSet.getFloat("price");
							price *= price_pizza;
						}
						System.out.println(product_id + ". " + description + " $" + price);
					}
					sub_order = scan.nextInt();
					scan.nextLine();
					
					
					query = ("select product_id, description, price from products where product_id = " + sub_order);
					resultSet = statement.executeQuery(query);
					while(resultSet.next()){
						product_id = resultSet.getInt("product_id");
						description = resultSet.getString("description");
						order_list.add(size_pizza + " " + description + " Pizza $" + price);
					}
					
					query = " insert into orders(order_id, product_id, customer_id, price, delivery_date, delivery_time, standing_order, delivery_basis, hold)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					p1 = connection.prepareStatement(query);
					
					p1.setInt(1, order_num);
					p1.setInt(2, product_id);
					p1.setInt(3, user_id);
					p1.setFloat(4, price);
					p1.setDate(5, new java.sql.Date(System.currentTimeMillis()) );  
					p1.setTime(6, new java.sql.Time(System.currentTimeMillis()));   
					p1.setInt(7, 0);
					p1.setString(8, "Once");
					p1.setInt(9, 0);
					p1.execute();
					System.out.println("Would you like to order anything else?(y/n)");
					String done_order = scan.nextLine();
					if(done_order.equalsIgnoreCase("y")){
						done_num++;
					} else {
						done = true;
					}
				} 
				if(order == 2){
					String query = "select product_id, description, price, taxable from products where category = ' Drink' and sale = 0";
					resultSet = statement.executeQuery(query);
					int product_id = 0;
					String description = null;
					while(resultSet.next()){
						taxable = resultSet.getInt("taxable");
						product_id = resultSet.getInt("product_id");
						description = resultSet.getString("description");
						if((taxable = resultSet.getInt("taxable")) == 1){
							price = resultSet.getFloat("price");
							price = price + (price * 0.13F);
						} else {
							price = resultSet.getFloat("price");
						}
						System.out.println(product_id + ". " + description + " $" + price);
					}
					sub_order = scan.nextInt();
					scan.nextLine();
					
					query = ("select product_id, description, price from products where product_id = " + sub_order);
					resultSet = statement.executeQuery(query);
					while(resultSet.next()){
						product_id = resultSet.getInt("product_id");
						description = resultSet.getString("description");
						price = resultSet.getFloat("price");
						order_list.add(description + " $" + price);
					}
					
					query = " insert into orders(order_id, product_id, customer_id, price, delivery_date, delivery_time, standing_order, delivery_basis, hold)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					p1 = connection.prepareStatement(query);
					
					p1.setInt(1, order_num);
					p1.setInt(2, product_id);
					p1.setInt(3, user_id);
					p1.setFloat(4, price);
					p1.setDate(5, new java.sql.Date(System.currentTimeMillis()) );  
					p1.setTime(6, new java.sql.Time(System.currentTimeMillis()));   
					p1.setInt(7, 0);
					p1.setString(8, "Once");
					p1.setInt(9, 0);
					p1.execute();
					System.out.println("Would you like to order anything else?(y/n)");
					String done_order = scan.nextLine();
					if(done_order.equalsIgnoreCase("y")){
						done_num++;
					} else {
						done = true;
					}
				}
				if(order == 3){
					String query = "select product_id, description, taxable, price from products where category = ' Side Dish' and sale = 0";
					resultSet = statement.executeQuery(query);
					int product_id = 0;
					String description = null;
					while(resultSet.next()){
						taxable = resultSet.getInt("taxable");
						product_id = resultSet.getInt("product_id");
						description = resultSet.getString("description");
						if((taxable = resultSet.getInt("taxable")) == 1){
							price = resultSet.getFloat("price");
							price = price + (price * 0.13F);
						} else {
							price = resultSet.getFloat("price");
						}
						System.out.println(product_id + ". " + description + " $" + price);
					}
					sub_order = scan.nextInt();
					scan.nextLine();
					
					query = " insert into orders(order_id, product_id, customer_id, price, delivery_date, delivery_time, standing_order, delivery_basis, hold)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					p1 = connection.prepareStatement(query);
					
					query = ("select product_id, description, price from products where product_id = " + sub_order);
					resultSet = statement.executeQuery(query);
					while(resultSet.next()){
						product_id = resultSet.getInt("product_id");
						description = resultSet.getString("description");
						price = resultSet.getFloat("price");
						order_list.add(description + " $" + price);
					}
					
					p1.setInt(1, order_num);
					p1.setInt(2, product_id);
					p1.setInt(3, user_id);
					p1.setFloat(4, price);
					p1.setDate(5, new java.sql.Date(System.currentTimeMillis()) );  
					p1.setTime(6, new java.sql.Time(System.currentTimeMillis()));   
					p1.setInt(7, 0);
					p1.setString(8, "Once");
					p1.setInt(9, 0);
					p1.execute();
					System.out.println("Would you like to order anything else?(y/n)");
					String done_order = scan.nextLine();
					if(done_order.equalsIgnoreCase("y")){
						done_num++;
					} else {
						done = true;
					}
				}
				if(order == 4){
					int product_id2 = 0;
					String query = "select product_id, description, price, taxable, category from products where sale = 1";
					resultSet = statement.executeQuery(query);
					int product_id = 0;
					String description = null;
					String category = null;
					while(resultSet.next()){
						taxable = resultSet.getInt("taxable");
						product_id = resultSet.getInt("product_id");
						description = resultSet.getString("description");
						category = resultSet.getString("category");
						if((taxable = resultSet.getInt("taxable")) == 1){
							price = resultSet.getFloat("price");
							price = price + (price * 0.13F);
						} else {
							price = resultSet.getFloat("price");
						}
						if(category.equalsIgnoreCase(" Pizza")){
							System.out.println(product_id + ". " + description + " Pizza $" + price);
						} else {
							System.out.println(product_id + ". " + description + " $" + price);
						}
					}
					System.out.println("On sale and combos: ");
					String combo_query = "select product_id, price, combination_num from combination";
					resultSet = statement.executeQuery(combo_query);
					product_id = 0;
					description = null;
					category = null;
					int combination_num = 0;
					int total_cost = 0;
					int count = 0;
					while(resultSet.next()){
						product_id = resultSet.getInt("product_id");
						price = resultSet.getInt("price");
						combination_num = resultSet.getInt("combination_num");
						String combo_query2 = "select product_id, description, taxable, price, category from products";
						resultSet2 = statement2.executeQuery(combo_query2);
						if(count != 0){
							System.out.print("/");
						} else{
							System.out.print("C" + combination_num + ". (");
						}
						while(resultSet2.next()){
							description = resultSet2.getString("description");
							product_id2 = resultSet2.getInt("product_id");
							if(product_id2 == product_id){
								category = resultSet2.getString("category");
								taxable = resultSet2.getInt("taxable");
								price = resultSet2.getInt("price");
								if((taxable = resultSet2.getInt("taxable")) == 1){
									price = resultSet2.getFloat("price");
									price = price + (price * 0.13F);
								} else {
									price = resultSet2.getFloat("price");
								}
								total_cost += price;
								if(category.equalsIgnoreCase(" Pizza")){
									System.out.print(description + " Pizza ");
									count++;
								} else {
									System.out.print(description + " ");
									count++;
								}
							}
						}
					}
					if(combination_num != 0){
						System.out.print("$" + total_cost + ")");
					}
					System.out.println("");
					
					String sub_order_string = scan.nextLine();
					if(sub_order_string.contains("C")){
						price = total_cost;
						sub_order = Integer.parseInt(sub_order_string.replaceAll("[\\D]", ""));
						query = ("select combination_num from combination where product_id = " + product_id2);
						resultSet = statement.executeQuery(query);
						while(resultSet.next()){
							combination_num = resultSet.getInt("combination_num");
							order_list.add("C" + combination_num + " " + description + " $" + total_cost);
						}
						query = "insert into orders(order_id, product_id, customer_id, price, delivery_date, delivery_time, standing_order, delivery_basis, hold)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
						p1 = connection.prepareStatement(query);
						p1.setInt(1, order_num);
						p1.setInt(2, product_id2);
						p1.setInt(3, user_id);
						p1.setFloat(4, total_cost);
						p1.setDate(5, new java.sql.Date(System.currentTimeMillis()));  
						p1.setTime(6, new java.sql.Time(System.currentTimeMillis())); 
						p1.setInt(7, 0);
						p1.setString(8, "Once");
						p1.setInt(9, 0);
						p1.execute();
					} else {
						sub_order = Integer.valueOf("sub_order_string");
						query = ("select product_id, description, price from products where product_id = " + sub_order);
						resultSet = statement.executeQuery(query);
						while(resultSet.next()){
							product_id = resultSet.getInt("product_id");
							description = resultSet.getString("description");
							price = resultSet.getFloat("price");
							order_list.add(description + " $" + price);
						}
					}
					query = "insert into orders(order_id, product_id, customer_id, price, delivery_date, delivery_time, standing_order, delivery_basis, hold)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					p1 = connection.prepareStatement(query);
					p1.setInt(1, order_num);
					p1.setInt(2, product_id);
					p1.setInt(3, user_id);
					p1.setFloat(4, price);
					p1.setDate(5, new java.sql.Date(System.currentTimeMillis())); 
					p1.setTime(6, new java.sql.Time(System.currentTimeMillis())); 
					p1.setInt(7, 0);
					p1.setString(8, "Once");
					p1.setInt(9, 0);
					p1.execute();
					System.out.println("Would you like to order anything else?(y/n)");
					String done_order = scan.nextLine();
					if(done_order.equalsIgnoreCase("y")){
						done_num++;
					} else {
						done = true;
					}
				}
			} catch (Exception e){
				System.out.println("error: " + e);
			}
		}
		
		order_num++;
		System.out.println("When do you want your order by? (HH:MM:SS)");
		
		String time = scan.nextLine();
		String query = "UPDATE orders SET delivery_time = ? WHERE customer_id = ?;";
		try {
			p1 = connection.prepareStatement(query);
			p1.setString(1, time);
			p1.setInt(2, user_id);
			p1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		System.out.println("Would you like to made that a standing order? (y/n)");
		String standing_order = scan.nextLine();
		if(standing_order.equalsIgnoreCase("y")){
			System.out.println("How often would you like this order: ");
			System.out.println("1. Daily");
			System.out.println("2. Weekly");
			System.out.println("3. Monthly");
			int basis = scan.nextInt();
			query = "UPDATE orders SET delivery_basis = ? AND standing_order = ? WHERE customer_id = ?;";
			try {
				p1 = connection.prepareStatement(query);
				switch(basis){
					case 1: p1.setString(1, "Daily");
							break;
					case 2: p1.setString(1, "Weekly");
							break;
					case 3:	p1.setString(1, "Monthly");
				}
				p1.setInt(2, 1);
				p1.setInt(3, user_id);
				p1.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error: " + e);
			}
		} else {
			try{
				query = ("select delivery_date, delivery_time from orders where customer_id = " + user_id + ";");
				resultSet = statement.executeQuery(query);
				Date delivery_date = null;
				Time delivery_time = null;
				while(resultSet.next()){
					delivery_date = resultSet.getDate("delivery_date");
					delivery_time = resultSet.getTime("delivery_time");
				}
				System.out.println("Your order has been completed, it will be delivered on " + delivery_date + " on " + delivery_time);
				
			} catch(Exception e){
				System.out.println("error: " + e);
			}
		}
	}

	private void displayCurrentOrders(){
		int order_id = 0;
		Time delivery_time = null;
		Date delivery_date = null;
		int customer_id = 0;
		int hold = 0;
		
		String query = "SELECT order_ID, customer_ID, delivery_time, delivery_date, hold FROM Orders";
		try{
			p1 = connection.prepareStatement(query);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				hold = resultSet.getInt("hold");
				order_id = resultSet.getInt("order_id");
				delivery_time = resultSet.getTime("delivery_time");
				delivery_date = resultSet.getDate("delivery_date");
				customer_id = resultSet.getInt("customer_id");
				if(hold == 0){
					System.out.println("Order ID: " + order_id + " Delivery Time: " + delivery_time + " Delivery Date: " + delivery_date + " Customer ID: " +customer_id);
				}
			}
		} catch(Exception e){
			System.out.println("error: " + e);
		}
		System.out.println("Which order would you like to accept? (order #): ");
		int order_num = scan.nextInt();
		orderAccepted(order_num);
	}
	
	private void orderAccepted(int order_id){
		
		int total_cost = 0;

		try { 
			
			int route = 0;
			String address_query = "SELECT route FROM Distance WHERE address IN " +
					"(SELECT address FROM Customers WHERE customer_ID = ?)";
			p1 = connection.prepareStatement(address_query);
			p1.setInt(1, user_id);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				route = resultSet.getInt("route");
			}
			
			String query = "insert into dispatch_ticket(customer_ID, order_ID, route)" + "values (?, ?, ?)";
			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			p1.setInt(2, order_id);
			p1.setInt(3, route);
			p1.execute();
			
			int cost = 0;
			int price = 0;
			String price_query = "SELECT SUM(price) AS 'price' FROM Products WHERE product_ID IN" +
					" (SELECT product_ID FROM orders WHERE order_ID = ?)";
			String cost_query = "SELECT cost FROM Cost WHERE distance = " +
					"(SELECT distance FROM Distance WHERE address = (SELECT address FROM Customers WHERE customer_ID = ?))";
			p1 = connection.prepareStatement(price_query);
			p1.setInt(1, order_id);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				price = resultSet.getInt("price");
			}
			p1 = connection.prepareStatement(cost_query);
			p1.setInt(1, user_id);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				cost = resultSet.getInt("cost");
			}
			

			total_cost = price + cost;
			
			query = "insert into bill(customer_ID, order_id, total_cost)" + "values (?, ?, ?)";
			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			p1.setInt(2, order_id);
			p1.setInt(3, total_cost);
			p1.execute();
		} catch(Exception e) {
			System.out.println("error: " + e);
		}
		
		
		try{
			float one_way_time = 0;
			int employee_ID = 0;
			Time delivery_time = null;
			Time return_time = null;
			
			String query = "SELECT distance, address FROM Distance WHERE address = " +
					"(SELECT address FROM Customers WHERE customer_ID = ?)";
			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				String address = resultSet.getString("address");
				one_way_time = resultSet.getFloat("distance");
				one_way_time = one_way_time / 0.007f;
			}
			
			query = "SELECT ADDTIME(now(), SEC_TO_TIME(?)) AS TIME";
			p1 = connection.prepareStatement(query);
			p1.setInt(1, (int)one_way_time);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				delivery_time = resultSet.getTime("TIME");
			}
			query = "SELECT ADDTIME(now(), SEC_TO_TIME(2 * ?)) AS TIME";
			p1 = connection.prepareStatement(query);
			p1.setInt(1, (int)one_way_time);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				return_time = resultSet.getTime("TIME");
			}
			
			query = "SELECT employee_ID FROM Delivery_people WHERE location = ' store\r'";
			p1 = connection.prepareStatement(query);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				employee_ID = resultSet.getInt("employee_ID");
			}
			
			query = "UPDATE delivery_people SET location = ? WHERE employee_id = ?";
			try {
				p1 = connection.prepareStatement(query);
				p1.setString(1, " delivery\r");
				p1.setInt(2, employee_ID);
				p1.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error: " + e);
			}
			
			query = " insert into transaction(customer_ID, price, delivery_date, delivery_time, delivery_person, time_dispatched, return_time, receive_payment)" + "values (?, ?, ?, ?, ?, ?, ?, ?)";
			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			p1.setInt(2, total_cost);
			p1.setDate(3, new java.sql.Date(System.currentTimeMillis()));
			p1.setTime(4, delivery_time); 
			p1.setInt(5, employee_ID);
			p1.setTime(6, new java.sql.Time(System.currentTimeMillis()));
			p1.setTime(7, return_time);
			p1.setInt(8, 0);
			p1.execute();

			
			query = "select * from orders where order_id = ?";
			int standing_order = 0;
			String basis = null;
			p1 = connection.prepareStatement(query);
			p1.setInt(1, order_id);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				standing_order = resultSet.getInt("standing_order");
				System.out.println(standing_order);
				int product_id = resultSet.getInt("product_id");
				int price = resultSet.getInt("price");
				basis = resultSet.getString("delivery_basis");
				
				Date weekly_delivery_date = null;
				query = "SELECT ADDDATE(CURDATE(), INTERVAL 7 DAY) AS DATE";
				p1 = connection.prepareStatement(query);
				resultSet = p1.executeQuery();
				while(resultSet.next()){
					weekly_delivery_date = resultSet.getDate("DATE");
				}
				
				Date monthly_delivery_date = null;
				query = "SELECT ADDDATE(CURDATE(), INTERVAL 31 DAY) AS DATE";
				p1 = connection.prepareStatement(query);
				resultSet = p1.executeQuery();
				while(resultSet.next()){
					monthly_delivery_date = resultSet.getDate("DATE");
				}
				
				if(standing_order == 1){
					if(basis.equalsIgnoreCase("weekly")){
						query = " insert into orders(order_id, product_id, customer_id, price, delivery_date, delivery_time, standing_order, delivery_basis, hold)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
						p1 = connection.prepareStatement(query);
						p1.setInt(1, order_id + 1);
						p1.setInt(2, product_id);
						p1.setInt(3, user_id);
						p1.setFloat(4, price);
						p1.setDate(5, weekly_delivery_date);  
						p1.setTime(6, new java.sql.Time(System.currentTimeMillis()));  
						p1.setInt(7, 0);
						p1.setString(8, "Weekly");
						p1.setInt(9, 0);
						p1.execute();
					}
					if(basis.equalsIgnoreCase("monthly")){
						query = " insert into orders(order_id, product_id, customer_id, price, delivery_date, delivery_time, standing_order, delivery_basis, hold)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
						p1 = connection.prepareStatement(query);
						
						p1.setInt(1, order_id + 1);
						p1.setInt(2, product_id);
						p1.setInt(3, user_id);
						p1.setFloat(4, price);
						p1.setDate(5, monthly_delivery_date); 
						p1.setTime(6, new java.sql.Time(System.currentTimeMillis()));   
						p1.setInt(7, 0);
						p1.setString(8, "Weekly");
						p1.setInt(9, 0);
						p1.execute();
					}
			}
			}
		} catch(Exception e) {
			System.out.println("error: " + e);
		}
		
	}
	
	private void receivePayment(){
		
		String query = "SELECT balance from customers where customer_id = ?";
		int balance = 0;
		try{
			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				balance = resultSet.getInt("balance");
			}
		} catch(Exception e){
			System.out.println("error: " + e);
		}
		System.out.println("Your balance is currently:  $" + balance);
		System.out.println("Your balance will be set to 0 after this");
		System.out.println("...");
		query = "UPDATE transaction SET receive_payment = 1 WHERE customer_id = ? AND receive_payment = 0";
		String query2 = "UPDATE customers SET balance = 0 WHERE customer_id = ?";
		try {
			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			p1.executeUpdate();
			
			p1 = connection.prepareStatement(query2);
			p1.setInt(1, user_id);
			p1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		System.out.println("Thank you for paying off your balance");
	}
	
	private void productQuery(){
		
		System.out.println("Would you like to search for a range of price? (y/n)");
		String answer = scan.nextLine();
		if(answer.equalsIgnoreCase("Y")){
			System.out.print("Lowest: ");
			int low = scan.nextInt();
			System.out.print("Highest: ");
			int high = scan.nextInt();
			scan.nextLine();
			
			System.out.println("Would you also like to search for a manufacturer? (y/n)");
			String answer2 = scan.nextLine();
			if(answer2.equalsIgnoreCase("Y")){
				System.out.print("Manufacturer: ");
				String manufacturer = scan.nextLine();
				try{
					String query = "select * from products where price >= " + low + " AND price <= " + high + " AND manufacturer = ?;";
						p1 = connection.prepareStatement(query);
						p1.setString(1, manufacturer);
						resultSet = p1.executeQuery();
						System.out.println("records from databases");
						while(resultSet.next()){
							String description = resultSet.getString("description");
							String category = resultSet.getString("category");
							float price = resultSet.getFloat("price");
							System.out.println(description + " " + category + " " + price);
						}
				} catch(Exception e){
					System.out.println("error: " + e);
				}
			} else {
				try{
					String query = "select * from products where price >= " + low + " AND price <= " + high + ";";

					resultSet = statement.executeQuery(query);
					System.out.println("records from databases");
					while(resultSet.next()){
						String description = resultSet.getString("description");
						String category = resultSet.getString("category");
						float price = resultSet.getFloat("price");
						System.out.println(description + " " + category + " " + price);
					}
				}catch(Exception e){
					System.out.println("error: " + e);
				}
			}
		} else {
			System.out.println("Would you like to search for a manufacturer? (y/n)");
			String answer2 = scan.nextLine();
			if(answer2.equalsIgnoreCase("Y")){
				System.out.print("Manufacturer: ");
				String manufacturer = scan.nextLine();
				try{
					String query = "select * from products where manufacturer = ?;";
						p1 = connection.prepareStatement(query);
						p1.setString(1, manufacturer);
						resultSet = p1.executeQuery();
						System.out.println("records from databases");
						while(resultSet.next()){
							String description = resultSet.getString("description");
							String category = resultSet.getString("category");
							float price = resultSet.getFloat("price");
							System.out.println(description + " " + category + " " + price);
						}
				} catch(Exception e){
					System.out.println("error: " + e);
				}
			} else {
				try{
					String query = "select * from products;";
						p1 = connection.prepareStatement(query);
						resultSet = p1.executeQuery();
						System.out.println("records from databases");
						while(resultSet.next()){
							String description = resultSet.getString("description");
							String category = resultSet.getString("category");
							float price = resultSet.getFloat("price");
							System.out.println(description + " " + category + " " + price);
						}
				} catch(Exception e){
					System.out.println("error: " + e);
				}
			}
		}
	}
	
	private void balanceQuery(){
		try{
			String query = ("SELECT balance FROM customers WHERE customer_id = ?;");

			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			resultSet = p1.executeQuery();
			int balance = 0;
			while(resultSet.next()){
				balance = resultSet.getInt("balance");
			}
			System.out.println("Your current balance is: $" + balance + ".");
			
		} catch(Exception e){
			System.out.println("error: " + e);
		}
	}

	private void transactionHistory(){
		System.out.println("From what time periods would you like to see your transactions");
		System.out.println("Earliest date(YYYY-MM-DD): ");
		String earliest = scan.nextLine();
		System.out.println("Latest date(YYYY-MM-DD): ");
		String latest = scan.nextLine();
		String query = "SELECT * FROM Transaction WHERE customer_ID = ? AND (delivery_date <= ?) AND (delivery_date >= ?)";
		try{
			p1 = connection.prepareStatement(query);
			p1.setInt(1, user_id);
			p1.setString(2, latest);
			p1.setString(3, earliest);
			resultSet = p1.executeQuery();
			while(resultSet.next()){  
				String transaction_id = resultSet.getString("transaction_id");
				Date delivery_date = resultSet.getDate("delivery_date");
				Time delivery_time = resultSet.getTime("delivery_time");
				float price = resultSet.getFloat("price");
				System.out.println(transaction_id + ". " + delivery_date + " " + delivery_time + " " + price);
			}
		} catch(Exception e){
			System.out.println("error: " + e);
		}
	}

	private void orderQuery(){
		System.out.println("You have the following orders: ");
		String query = "SELECT order_id, product_id, price, delivery_time, delivery_basis, delivery_date FROM Orders WHERE customer_ID = " + user_id + ";";
		try{
			Time delivery_time = null;
			Date delivery_date = null;
			p1 = connection.prepareStatement(query);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				String description = null;
				String category = null;
				String size = null;
				boolean pizza = false;
				int new_price = 0;
				delivery_date = resultSet.getDate("delivery_date");
				int product_id = resultSet.getInt("product_id");
				int order_id = resultSet.getInt("order_id");
				System.out.print("Order id:" + order_id + " ");
				String delivery_basis = resultSet.getString("delivery_basis");
				int original_price = resultSet.getInt("price");
				delivery_time = resultSet.getTime("delivery_time");
				String new_query = "SELECT description, price, category FROM Products WHERE product_id = " + product_id + ";";
				p1 = connection.prepareStatement(new_query);
				resultSet2 = p1.executeQuery();
				while(resultSet2.next()){
					new_price = resultSet2.getInt("price");
					description = resultSet2.getString("description");
					if(resultSet2.getString("category").equalsIgnoreCase(" Pizza")){
						category = " Pizza";
						pizza = true;
						if((new_price / 0.75) == original_price){
							size = "Small";
						} else if(new_price == original_price){
							size = "Medium";
						} else if((new_price / 1.25) == original_price){
							size = "Large";
						} else {
							size = "X-Large";
						}
					}
				}
				if(pizza){
					System.out.println(size + " " + description + " Pizza $" + new_price + " " + delivery_basis);
				} else {
					System.out.println(description + " $" + original_price + " " + delivery_basis);
				}
				System.out.println("Expected Delivery time: " + delivery_time + " and Expected Delivery day: " + delivery_date); 
				System.out.println();
				putOnHold();
			}
		} catch(Exception e){
			System.out.println("error: " + e);
		}
	}
	
	private void putOnHold(){
		
		boolean done = false;
		int count = 0;
		while(!done){
			if(count == 0){
				System.out.println("Would you like to put on hold any standing orders? (y/n)");
			}
			String answer = scan.nextLine();
			if(answer.equalsIgnoreCase("y")){
				System.out.println("Enter the order id of the order you'd like to hold: ");
				int order_id = scan.nextInt();
				scan.nextLine();
				String query = "UPDATE orders SET hold = 1 WHERE order_id = ?;";
				try {
					p1 = connection.prepareStatement(query);
					p1.setInt(1, order_id);
					p1.executeUpdate();
				} catch (SQLException e) {
					System.out.println("error: " + e);
				}
			}
			System.out.println("Do you have any other standing orders to place on hold? (y/n)");
			if(answer.equalsIgnoreCase("n")){
				done = true;
			}
			count++;
		}
	}
	
	private void bestSellerReport(){
		String description = null;
		boolean pizza = false;
		
		String query = "SELECT Orders.product_ID as num, count(*) as order_count, Products.product_ID FROM Orders, Products" +
				" WHERE (delivery_date <= ?) AND (delivery_date >= ?) "
					   + "GROUP BY num DESC";
		System.out.println("From what time periods would you like to see the best selling products");
		System.out.println("Earliest date(YYYY-MM-DD): ");
		String earliest = scan.nextLine();
		System.out.println("Latest date(YYYY-MM-DD): ");
		String latest = scan.nextLine();
		System.out.println("What type of product would you like to see?");
		String category = scan.nextLine();
		String real_category = " ";
		System.out.println(real_category);
		real_category.concat(category);
		String category2 = null;
		int order_count = 0;
		try{
			p1 = connection.prepareStatement(query);
			p1.setDate(1, java.sql.Date.valueOf(latest));
			p1.setDate(2, java.sql.Date.valueOf(earliest));
			resultSet = p1.executeQuery();
			while(resultSet.next()){ 
				int product_id = resultSet.getInt("num");
				order_count = resultSet.getInt("order_count");
				String new_query = "SELECT description, category FROM Products WHERE product_id = " + product_id + ";";
				p1 = connection.prepareStatement(new_query);
				resultSet2 = p1.executeQuery();
				while(resultSet2.next()){
					category2 = resultSet2.getString("category");

					description = resultSet2.getString("description");
					if(resultSet2.getString("category").equalsIgnoreCase(" Pizza")){
						category2 = " Pizza";			
						pizza = true;
					}
				}
				if(pizza && category.equalsIgnoreCase(category2)){
					System.out.println(description + " Pizza ");
				} else if(!pizza && category.equalsIgnoreCase(category2)){
					System.out.println(description);
				}
			}
		} catch(Exception e){
			System.out.println("error: " + e);
		}
	}
	
	private void worstSellerReport(){
		String description = null;
		boolean pizza = false;
		
		String query = "SELECT Orders.product_ID as num, count(*) as order_count, Products.product_ID FROM Orders, Products" +
				" WHERE (delivery_date <= ?) AND (delivery_date >= ?) "
					   + "GROUP BY num ASC";
		System.out.println("From what time periods would you like to see the best selling products");
		System.out.println("Earliest date(YYYY-MM-DD): ");
		String earliest = scan.nextLine();
		System.out.println("Latest date(YYYY-MM-DD): ");
		String latest = scan.nextLine();
		System.out.println("What type of product would you like to see?");
		String category = scan.nextLine();
		String real_category = " ";
		System.out.println(real_category);
		real_category.concat(category);
		String category2 = null;
		int order_count = 0;
		try{
			p1 = connection.prepareStatement(query);
			p1.setDate(1, java.sql.Date.valueOf(latest));
			p1.setDate(2, java.sql.Date.valueOf(earliest));
			resultSet = p1.executeQuery();
			while(resultSet.next()){ 
				int product_id = resultSet.getInt("num");
				order_count = resultSet.getInt("order_count");
				String new_query = "SELECT description, category FROM Products WHERE product_id = " + product_id + ";";
				p1 = connection.prepareStatement(new_query);
				resultSet2 = p1.executeQuery();
				while(resultSet2.next()){
					category2 = resultSet2.getString("category");

					description = resultSet2.getString("description");
					if(resultSet2.getString("category").equalsIgnoreCase(" Pizza")){
						category2 = " Pizza";			
						pizza = true;
					}
				}
				if(pizza && category.equalsIgnoreCase(category2)){
					System.out.println(description + " Pizza ");
				} else if(!pizza && category.equalsIgnoreCase(category2)){
					System.out.println(description);
				}
			}
		} catch(Exception e){
			System.out.println("error: " + e);
		}
	}

	private void itemOnSale(){
		System.out.println("Do you want to add a single item or a combination on sale? (s/c)");
		String answer = scan.nextLine();
		
		if(answer.equalsIgnoreCase("s")){
			System.out.println("Which product ID do you want to put on sale?");
			int item = scan.nextInt();
			String query = "select product_id, description, category from products where product_id = " + item;
			int product_id = 0;
			String description = null;
			String category = null;
			try{
				p1 = connection.prepareStatement(query);
				resultSet = p1.executeQuery();
				while(resultSet.next()){
					product_id = resultSet.getInt("product_id");
					category = resultSet.getString("category");
					description = resultSet.getString("description");
					String first_name = null;
					String last_name = null;
					query = "select first_name, last_name, customer_id from customers where customer_id = ANY " +
							"(select customer_id from orders where product_id = " + product_id + ")"; 
						p1 = connection.prepareStatement(query);
						resultSet = p1.executeQuery();
						while(resultSet.next()){
							first_name = resultSet.getString("first_name");
							last_name = resultSet.getString("last_name");
							if(category.equalsIgnoreCase(" Pizza")){
								System.out.print(first_name + " " + last_name + ", " + description + " Pizza is on sale!" );
							} else {
								System.out.print(first_name + " " + last_name + ", " + description + " is on sale!");
							}
						}
				}
				query = "UPDATE Products SET price = price * 0.80, sale = true WHERE sale = false AND product_ID = " + product_id;
				p1 = connection.prepareStatement(query);
				p1.executeUpdate();
			} catch(Exception e){
				System.out.println("error: " + e);
			}
		} else {
			int product_id1 = 0;
			int product_id2 = 0;
			String category1 = null;
			String description1 = null;
			String category2 = null;
			String description2 = null;
			System.out.println("What's the first product ID you want to put on sale?");
			int product1 = scan.nextInt();
			String query = "select product_id, category, description from products where product_id = " + product1;
			try{
				p1 = connection.prepareStatement(query);
				resultSet = p1.executeQuery();
				while(resultSet.next()){
					category1 = resultSet.getString("category");
					description1 = resultSet.getString("description");
					product_id1 = resultSet.getInt("product_id");
				}
			} catch(Exception e){
				System.out.println("error: " + e);
			}
			System.out.println("What's the second product ID you want to put on sale?");
			int product2 = scan.nextInt();
			query = "select product_id, category, description from products where product_id = " + product2;
			try{
				p1 = connection.prepareStatement(query);
				resultSet = p1.executeQuery();
				while(resultSet.next()){
					category2 = resultSet.getString("category");
					description2 = resultSet.getString("description");
					product_id2 = resultSet.getInt("product_id");
				}
			} catch(Exception e){
				System.out.println("error: " + e);
			}
			System.out.println("What price would you like to make it?");
			float price = scan.nextFloat();
			query = "insert into combination(product_ID, price, combination_num)" + "values (?, ?, ?)";
			try{
				p1 = connection.prepareStatement(query);
				p1.setInt(1, product_id1);
				p1.setFloat(2, price);
				p1.setInt(3, 2);
				p1.execute();
				
				p1 = connection.prepareStatement(query);
				p1.setInt(1, product_id2);
				p1.setFloat(2, price);
				p1.setInt(3, 2);
				p1.execute();
			} catch(Exception e){
				System.out.println("error: " + e);
			}
			boolean true1 = false;
			boolean true2 = false;
			query = "select product_id, category, description from products where product_id = " + product1;
			try{
				p1 = connection.prepareStatement(query);
				resultSet = p1.executeQuery();
				while(resultSet.next()){
					String first_name = null;
					query = "select first_name, last_name, customer_id from customers where customer_id = ANY " +
							"(select customer_id from orders where product_id = " + product_id1 + ")"; 
						p1 = connection.prepareStatement(query);
						resultSet = p1.executeQuery();
						while(resultSet.next()){
							first_name = resultSet.getString("first_name");
							System.out.println(first_name);
							if(first_name != null){
								System.out.println(true1);
								true1 = true;
							}
						}
				}
			} catch(Exception e){
				System.out.println("error: " + 3);
			}
			query = "select product_id, category, description from products where product_id = " + product2;
			try{
				p1 = connection.prepareStatement(query);
				resultSet = p1.executeQuery();
				while(resultSet.next()){
					String first_name = null;
					String last_name = null;
					query = "select first_name, last_name, customer_id from customers where customer_id = ANY " +
							"(select customer_id from orders where product_id = " + product_id2 + ")"; 
						p1 = connection.prepareStatement(query);
						resultSet = p1.executeQuery();
						while(resultSet.next()){
							first_name = resultSet.getString("first_name");
							last_name = resultSet.getString("last_name");
							if(first_name != null){
								true2 = true;
								if(true1 && true2){
									if(category1.equalsIgnoreCase(" Pizza") && category2.equalsIgnoreCase(" Pizza")){
										System.out.print(first_name + " " + last_name + ", " + description1 + " " + description2 + " Pizza is on sale and" );
									} else if(category1.equalsIgnoreCase(" Pizza") && !category2.equalsIgnoreCase(" Pizza")){
										System.out.print(first_name + " " + last_name + ", " + description1 + "Pizza and " + description2+ " is on sale!");
									} else if(category2.equalsIgnoreCase(" Pizza") && !category1.equalsIgnoreCase(" Pizza")){
										System.out.print(first_name + " " + last_name + ", " + description2 + "Pizza and " + description1+ " is on sale!");
									} else {
										System.out.println(first_name + " " + last_name + ", " + description1 + " and " + description2 + " is on sale!");
									}
								}
							}
						}
				}
			} catch(Exception e){
				System.out.println("error: " + 3);
			}
		}
	}

	private void delinquencyReport(){
		String query = "SELECT Transaction.customer_ID FROM Transaction, Customers WHERE Customers.balance < -200 AND " +
				"DATEDIFF(CURDATE(), Transaction.delivery_date) >= 20 " +
				"AND Customers.customer_ID = Transaction.customer_ID";
		try{
			p1 = connection.prepareStatement(query);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				int customer_id = resultSet.getInt("customer_ID");
				String new_query = "SELECT first_name, last_name FROM customers WHERE customer_id = " + customer_id + ";";
				p1 = connection.prepareStatement(new_query);
				resultSet2 = p1.executeQuery();
				while(resultSet2.next()){
					String first_name = resultSet2.getString("first_name");
					String last_name = resultSet2.getString("last_name");
					System.out.println(first_name + " " + last_name + " your balance has not been paid off in 20 days, please do so soon or your account will be banned.");
				}
			}
		}  catch(Exception e){
			System.out.println("error: " + e);
		}
	}

	private void suggestProduct(){
		String query = "SELECT * FROM Products WHERE manufacturer = " +
				"(SELECT manufacturer FROM Products WHERE product_ID = " +
				"(SELECT product_ID FROM (SELECT product_ID, COUNT(product_ID) AS rep FROM Orders " +
				"GROUP BY product_ID ORDER BY rep DESC) AS t limit 1))";
		
		System.out.println("Here's a suggested product based on your previous purchasing history: ");
		try{
			p1 = connection.prepareStatement(query);
			resultSet = p1.executeQuery();
			while(resultSet.next()){
				String description = resultSet.getString("description");
				String category = resultSet.getString("category");
				float price = resultSet.getFloat("price");
				int taxable = resultSet.getInt("taxable");
				if((taxable = resultSet.getInt("taxable")) == 1){
						price = price + (price * 0.13F);
				}
				if(category.equalsIgnoreCase(" Pizza")){
					System.out.println(description + " " + category + " $" + price);
				} else {
					System.out.println(description + " $" + price);
				}
			}
		} catch(Exception e){
			System.out.println("error: " + e);
		}
	}
}

