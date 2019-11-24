package jdbcdemo;
import java.sql.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Driver {
	
	public static void main(String[] args)
	{
		
		System.out.println("\t\t\tonlineshopping.com");
		System.out.println("\t\t\t" +  new Timestamp(System.currentTimeMillis()) + "\t\t\t" );
		String userid = auth();
		choices(userid);
	}

	


public static String auth()
{
	    
		Scanner s = new Scanner(System.in);
						for( ; ; )
						{
							System.out.print("\n\n\n 1) Sign in\n\n\n 2) Sign up\n\n\n 3) Exit website\n\n");
							int choice = s.nextInt();
							switch(choice)
							{
								case 1:
								{
									s.nextLine();
									System.out.print("\n\nEnter your username: ");
									String susername = s.nextLine();
								
									try 
									{
														
										Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
										Statement stmt = con.createStatement();
										ResultSet rs = stmt.executeQuery("SELECT username, AES_DECRYPT(password,'key') from userinfo");
										int flag = 0;
				
										while(rs.next()) 
										{ 
											if( (susername.compareTo(rs.getString("username"))) == 0)
											{	flag = 1;
											break;}
										}
												
											if(flag == 1)
												{System.out.print("\n\nEnter your password: ");
												String spassword = s.nextLine();
												if(spassword.compareTo(rs.getString("AES_DECRYPT(password,'key')")) == 0)
												{
													System.out.println("Welcome, " + rs.getString("username") + "!");
													return susername;
												}
												else 
												{
													System.out.println("Incorrect password, try again.");
													break;
												}
												}
											if(flag == 0) { System.out.println("Username does not exist, try again.");}
											
									
									
									}
									catch (Exception exc) 
									{ 
										exc.printStackTrace();
									}
									
							} break;
							
						case 2:
								{
										
									System.out.println("\n\n\n\nREGISTRATION:\n\n\n\n");
									s.nextLine();
									System.out.println("USER INFORMATION: ");
									System.out.println("Enter your Name: ");
									String name = s.nextLine();
									System.out.println("Enter Username: ");
									String username = s.nextLine();
									System.out.println("Enter a valid email-id: ");
									String emailid = s.nextLine();
									
				
									System.out.println("Enter a password: ");
									String password = s.nextLine();
									
									System.out.println("SHIPPING INFORMATION: ");
									System.out.println("Enter Zipcode: ");
									int zipcode = s.nextInt();
									s.nextLine();
									System.out.println("Enter a valid shipping address:");
									String address = s.nextLine();
									
									System.out.println("CONTACT INFORMATION: ");
									System.out.println("Enter mobile number:");
									String mobile = s.nextLine();
									System.out.println("Enter a landline number:");
									String landline = s.nextLine();
									
									//connection 2
									try
									{
										Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
										PreparedStatement prep = 
												con.prepareStatement("INSERT INTO userinfo (name,username,password,emailid,zipcode,address,mobile,landline) VALUES (?,?,AES_ENCRYPT(?,?),?,?,?,?,?)");
										prep.setString(1, name);
										prep.setString(2, username);
										prep.setString(3, password);
										prep.setString(4, "key");
										prep.setString(5, emailid);
										prep.setInt(6, zipcode);
										prep.setString(7, address);
										prep.setString(8, mobile);
										prep.setString(9, landline);
										prep.executeUpdate();
										
										
										System.out.println("BILLING INFORMATION: ");
										for ( ; ; )
										{
										System.out.println("1) Debit \t 2) Credit ");
										int ch = s.nextInt();
										if(ch == 1 )
										{	s.nextLine();
											System.out.println("Enter Debit Card Information: ");
											System.out.println("Card number: ");
											String debitcardno = s.nextLine();
											System.out.println("Expiration date: mm/yy ");
											String debitcardexp = s.nextLine();
											System.out.println("CVV (Given at the back of the card): ");
											int debitcardcvv = s.nextInt();
											try
											{
											PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET debitcardno = AES_ENCRYPT(?,?), debitcardexp = ? , debitcardcvv = AES_ENCRYPT(?,?) WHERE password = AES_ENCRYPT(?,?) ");
											prep1.setString(1, debitcardno);
											prep1.setString(2, "key");
											prep1.setString(3, debitcardexp);
											prep1.setInt(4, debitcardcvv);
											prep1.setString(5, "key");
											prep1.setString(6, password);
											prep1.setString(7, "key");
											prep1.executeUpdate();
											}
											catch (Exception exc) 
											{ 
												exc.printStackTrace();
											}
										
										}
										else
										{
											s.nextLine();
											System.out.println("Enter Credit Card Information: ");
											System.out.println("Card number: ");
											String creditcardno = s.nextLine();
											System.out.println("Expiration date: mm/yy ");
											String creditcardexp = s.nextLine();
											System.out.println("CVV (Given at the back of the card): ");
											int creditcardcvv = s.nextInt();
											//prep.close();
											try
											{
											PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET creditcardno = AES_ENCRYPT(?,?), creditcardexp = ? , creditcardcvv = AES_ENCRYPT(?,?) WHERE password =AES_ENCRYPT(?,?)");
											prep1.setString(1, creditcardno);
											prep1.setString(2, "key");
											prep1.setString(3, creditcardexp);
											prep1.setInt(4, creditcardcvv);
											prep1.setString(5, "key");
											prep1.setString(6, password);
											prep1.setString(7, "key");
											prep1.executeUpdate();
											}
											catch (Exception exc) 
											{ 
												exc.printStackTrace();
											}
											
										
									}
										
										System.out.println("Press 2 if you want to set up more cards else press any other number of move forward: ");
										int cho = s.nextInt();
										if(cho!=2) break;
										
									
									}
										try
										{
											Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshoppingcart","root", "root");
											Statement stmt = con1.createStatement();
											String sql = "CREATE TABLE cart" + username + "(num int PRIMARY KEY AUTO_INCREMENT,name varchar(100),price double,shipping_price double, quantity int)";
											stmt.executeUpdate(sql);
											
										}
										catch (Exception exc) 
										{ 
											exc.printStackTrace();
										}
										System.out.println("\n\nProfile created successfully, log in to start shopping!:");
										return username;
										
								
								}
								catch (Exception exc) 
								{ 
									exc.printStackTrace();
								}
						}break;
									
									
						case 3:
						{  System.exit(0); }		break;
						default:
							System.out.println("Invalid input, try again: ");
							}
						}
		
		
		

	
	}
		

public static void choices(String userid)
{
	
	Scanner s = new Scanner(System.in);
	for( ; ; )
	{	
		System.out.println("\n\n1) Log Out\n\n2) View categories\n\n3) Search for a product\n\n4) View items in cart\n\n5) Change customer information\n\n");
			//int ch1 = s.nextInt(); 
			char ch1 = s.next().charAt(0);
			
			switch(ch1)
		{
		case '1':
		{
			System.out.println("Logging you out ...");
			for (int i = 0; i < 1000; ++i) System.out.println();
			System.exit(0);
		}break;
		
		case '2': 
			categ(userid); break;
		case '3': 
		{
			String arr[] = {"health","electronics","food","games","clothes","home"};
			s.nextLine();
			System.out.println("Input a search value to check for the existence of a product: ");
			String searchstr = s.nextLine();
			try
			{	
				
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
				Statement stmt = con.createStatement();
				Statement stmt1 = con.createStatement();
				int flag = 0;
				for(int i =0;i<6;i++)
				{
					ResultSet rs = stmt.executeQuery("SELECT name,num from " + arr[i]);
					while(rs.next())
					{
					boolean val = rs.getString("name").contains(searchstr);
					while(val)
					{
						flag = 1;
						ResultSet rs1 = stmt1.executeQuery("SELECT * FROM " + arr[i] +  " WHERE num=" + rs.getString("num"));
						rs1.next();
						System.out.printf("%s\t%25s\t%25s\t%25s\t%25s\t%25s\n\n",rs1.getInt("num"),rs1.getString("id"),rs1.getString("name"),rs1.getDouble("price"), rs1.getDouble("shipping_price"), rs1.getString("description"));
						break;
					}
					
					
					
				}
					if(flag == 0)
					{
						System.out.println("Item not available!");
						break;
					}
			}		
				
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
			}
			
		}
		break;
		case '4': cart(userid); break;
		case '5': updateuservalues(userid);
		default: System.out.println("Invalid input, try again: ");
		}
			//s.next();
	}
  
}

public static void categ(String userid)
{
	Scanner s = new Scanner(System.in);
			
				try {
				
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select * from CATEGORY");
				System.out.println("\t\t_categories_\t\t");
				while(rs.next()) {
					System.out.println(rs.getInt("num") + ")" + rs.getString("name_of_category") + ", (" + rs.getString("number_of_products")+" products)\n\n" );
				}
				
				
				
				
					System.out.println("Choose a category you want to browse in, else click 0 to return to previous page:\n\n");
					int ch2 = s.nextInt();
					if(ch2 == 0) return;
					String str="\0";
					switch(ch2)
					{
					case 1:{ str = "health";} break;
					case 2:{ str = "electronics";} break;
					case 3:{ str = "food";} break;
					case 4:{ str = "games";} break;
					case 5:{ str = "clothes";} break;
					case 6:{ str = "home";} break;
					default: System.out.println("Invalid input, try again: "); return;
					}
					
					try
					{
						String sql = "select * from " + str;
					
					ResultSet rs1 = stmt.executeQuery(sql);
					System.out.printf("%s\t%25s\t%25s\t%25s\t%25s\t%30s\n\n","Number","Product Id","Product name","Product price","Shipping price","Description");
					
					while(rs1.next())
					{
						
						System.out.printf("%s\t%25s\t%25s\t%25s\t%25s\t%25s\n\n",rs1.getInt("num"),rs1.getString("id"),rs1.getString("name"),rs1.getDouble("price"), rs1.getDouble("shipping_price"), rs1.getString("description"));
					
					}
					}
					catch (Exception exc) 
					{ 
						exc.printStackTrace();
					}
					choices2(str,userid);
				
				
			}catch (Exception exc) { 
					exc.printStackTrace();
				}

				
				return;
				
}
			

public static void choices2(String table,String userid)
{
	try
	{
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
	Scanner s = new Scanner(System.in);
	System.out.println("\n\n1) Sort items \n\n 2) Add an item to the cart 3)View cart\n\n");
	int ch = s.nextInt();
	switch(ch)
	{
	case 1:
	{
		sort(table,userid); 
		}break;
	case 2: 
	{	
		System.out.println("Input the number of the product you want to add to your cart: ");
		int num = s.nextInt();
		System.out.println("Enter quantity: ");
		int quantity = s.nextInt();
		 try
		 {
			 Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshoppingcart","root", "root");
		
		 Statement stmt1 = con.createStatement();
		 ResultSet rs = stmt1.executeQuery("SELECT name AS name, price AS price, shipping_price AS shipping_price FROM " + table + " WHERE num =" + num);
		rs.next();
		PreparedStatement prep = con1.prepareStatement("INSERT INTO cart" + userid + "(name,price,shipping_price,quantity) VALUES (?,?,?,?)");
		prep.setString(1, rs.getString("name"));
		prep.setDouble(2, rs.getDouble("price"));
		prep.setDouble(3, rs.getDouble("shipping_price"));
		prep.setInt(4, quantity);
		prep.executeUpdate();
		 System.out.println("Item has been added to cart!");
		 
	}catch (Exception exc) 
			{ 
		exc.printStackTrace();
	}
	}break;
	case 3: cart(userid); break;
	default: System.out.println("Invalid input,");
	}
	}catch (Exception exc) { 
		exc.printStackTrace();
	}
	
}
public static void sort(String table,String userid)
{
	try
	{
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
	String str = "\0";
	String sort = "\0";
	Scanner s = new Scanner(System.in);
	System.out.println("1) Sort according to Product name\n\n2) Sort according to Product price\n\n3)Sort according to Shipping price");
	int ch1 = s.nextInt();
	System.out.println("1) Highest to lowest/A-Z\n\n2) Lowest to highest/Z-A\n\n");
	int order = s.nextInt();
	if(order == 1) sort = "ASC";
	else if(order == 2) sort = "DESC";
	else System.out.println("Invalid input, try again: ");
	switch(ch1)
	{
	case 1: 
		
			str = "name";
			
			
		 break;
	case 2:
		str = "price";
			
		break;
	case 3: str = "shipping_price"; break;
	default: System.out.println("Invalid input: ");
	}
	
	try
	{
	String sql = "SELECT * FROM " + table + " ORDER BY " + str + " " + sort;
	Statement stmt1 = con.createStatement();
	ResultSet rs1 = stmt1.executeQuery(sql);
	
	while(rs1.next()) {
		
		System.out.printf("%s\t%25s\t%25s\t%25s\t%25s\t%25s\n\n",rs1.getInt("num"),rs1.getString("id"),rs1.getString("name"),rs1.getDouble("price"), rs1.getDouble("shipping_price"), rs1.getString("description"));
	
	}
	
	}
	catch (Exception exc) 
	{ 
		exc.printStackTrace();
	}
	}
	catch (Exception exc) 
	{ 
		exc.printStackTrace();
	}

choices2(table,userid);

}

public static void cart(String userid)
{
	Scanner s = new Scanner(System.in);
	try
	{
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshoppingcart","root", "root");
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from cart" + userid);
		System.out.printf("\t%25s\t%25s\t%25s\t%25s\t\n\n","Product Name","Product Price","Shipping Price","Quantity");
		while(rs.next())
		{
			System.out.printf("\t%3d\t%25s\t%25s\t%25s\t%25s\t\n\n",rs.getInt("num"),rs.getString("name"),rs.getDouble("price"),rs.getDouble("shipping_price"),rs.getInt("quantity"));
		}
		
	
	for ( ; ; )
	{
	System.out.println("1) Checkout\n\n 2) Delete item\n\n 3) Go back to add more items\n\n");
	int ch = s.nextInt();
	switch(ch)
	{
	case 1: 
		{
			System.out.println("Proceeding to checkout ...");
			try
			{
				Statement stmt1 = con.createStatement();
				ResultSet rs1 = stmt1.executeQuery("select * from cart" + userid);
				System.out.printf("%25s\t   \t %s\n\n" ,"Product Name","Quantity");
				while(rs1.next())
				{
					System.out.printf("%25s\t x \t%2d \t\t %2f\n\n",rs1.getString("name"),rs1.getInt("quantity"),((rs1.getDouble("price") + rs1.getDouble("shipping_price")) * rs1.getInt("quantity")));
				}
				
				try
				{
					Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
					Statement stmt2 = con1.createStatement();
					ResultSet rs2 = stmt2.executeQuery("SELECT AES_DECRYPT(debitcardno,'key'),AES_DECRYPT(creditcardno,'key'),AES_DECRYPT(debitcardcvv,'key'),AES_DECRYPT(creditcardcvv,'key'),creditcardexp,debitcardexp,address,zipcode,username from userinfo WHERE username = '" + userid + "'");
					
					rs2.next();
					System.out.printf("%25s\n\n%5d\n\n",rs2.getString("address"),rs2.getInt("zipcode"));
					for ( ; ; )
					{System.out.println("Choose an available card: ");
					System.out.println("1) Debit card \t\t 2) Credit card");
					int cardch = s.nextInt();
					if(cardch == 1)
					{
						System.out.printf("%25s \n\n %25s", rs2.getString("AES_DECRYPT(debitcardno,'key')"), rs2.getString("debitcardexp"));
						
						System.out.println("Enter CVV for chosen card: ");
						int cvv = s.nextInt();
						
						if(cvv == rs2.getInt("AES_DECRYPT(debitcardcvv,'key')"))
						{
							System.out.println("CVV matched");
							break;
						}
						else {
							System.out.println("Wrong CVV, try again:");
						
						}
						
					}
					else if(cardch ==2)
					{
						System.out.printf("%25s \n\n %25s", rs2.getString("AES_DECRYPT(creditcardno,'key')"), rs2.getString("creditcardexp"));
						System.out.println("Enter CVV for chosen card: ");
						int cvv = s.nextInt();
						
						if(cvv == (rs2.getInt("AES_DECRYPT(creditcardcvv,'key')")))
						{
							System.out.println("CVV matched");
							break;
							
						}
						else {
							System.out.println("Wrong CVV, try again:");
						
						}
					}
					else 
					{
						System.out.println("Wrong choice, try again: ");
					}
					}
					System.out.println("Card details matched, move on to Confirmation of purchase?? This is final and you will be charged upon answering: (y/n)");
					char chc = s.next().charAt(0);
					if( chc == 'y' || chc == 'Y')
						{
						System.out.println("Confirming purchase and generating invoice ...");
						fin(userid);
						}
					else
					{
						choices(userid);
					}
					
					
					
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
				}
				
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
			}
			
		}break;
	case 2: 
	{ 
		try
		{
			System.out.println("Enter number of product you want to delete: ");
			int num = s.nextInt();
			Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshoppingcart","root", "root");
			Statement stmt1 = con1.createStatement();
			stmt1.executeUpdate("DELETE from cart"+userid+" where num ="+num);
		}catch(Exception exc)
		{
			exc.printStackTrace();
		}
	}break;
	case 3:
		choices(userid);
		break;
		default: System.out.println("Wrong choice, try again:" );
	}
	}
	}

catch(Exception exc)
{
	exc.printStackTrace();
}
}

public static void fin(String userid)
{
	System.out.println("Purchase confirmed, generating invoice: ");
	
	String FILENAME = "C:\\Users\\vidhya\\file"+userid +".txt";

		BufferedWriter bw = null;
		FileWriter fw = null;

			try
			{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
			Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshoppingcart","root", "root");
			Statement stmt = con.createStatement();
			Statement stmt1 = con1.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT name, emailid,zipcode,address FROM userinfo where username ="+userid);
			rs.next();
			
			ResultSet rs1 = stmt1.executeQuery("SELECT * FROM cart"+userid);
			
			String content = "Name: " + rs.getString("name") +"\t"+ "Email-id: " + rs.getString("emailid") + "\t";
			String content2 = "Address: " + rs.getString("address") + "\t" + "Zipcode: " + rs.getString("zipcode") + "\t";
			
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.write(content2);
			while(rs1.next())
			{	System.out.println();
				String content1 = "Product: " + rs1.getString("name") +"\t"+"\t" + "Quantity: "+"\t" + rs1.getInt("quantity"); 
				bw.write(content1);
				System.out.println();
			}

			System.out.println("Invoice generated");
			
			
			
		}catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
			
			try
			{
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshoppingcart","root", "root");
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM cart" + userid);
			}catch (Exception e) {

				e.printStackTrace();

			}
			choices(userid);
			return;
}


	public static void updateuservalues(String userid)
	{
		Scanner s = new Scanner(System.in);
		System.out.println("Do you want to change/update any user information? You can always do this later. (y/n)");
		char c = s.next().charAt(0);
		switch(c)
		{
		case 'y': case 'Y':  
		{
			System.out.println("Enter the field you want to update: ");
			System.out.println("\n\n1) Password\n\n2) Username\n\n3) Mobile number\n\n4) Landline number\n\n5) Address\n\n6) Email-id\n\n7) Card details\n\n");
			int choice = s.nextInt();
			String str = "\0";
			switch(choice)
			{
			
			case 1:
			{	s.nextLine();
				System.out.println("Enter a new password: ");
				String password = s.nextLine();
				try
				{
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
					PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET password = AES_ENCRYPT(?,?) WHERE username = ?");
					prep1.setString(1, password);
					prep1.setString(2, "key");
					prep1.setString(3, userid);
					prep1.executeUpdate();
				}catch (Exception e) {

					e.printStackTrace();

				}
			}break;
			case 2: 
			case 3: 
			case 4:
			case 5: 
			case 6:
			{
				try
				{	s.nextLine();
					System.out.println("Enter new value: ");
					String newval = s.nextLine();
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");if(choice == 3) str = "mobile";
					if(choice == 2)
					{	PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET username = ? WHERE username = ?");
						prep1.setString(1, newval);
						prep1.setString(2, userid);
						prep1.executeUpdate();
					}
					if(choice == 3)
					{	PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET mobile = ? WHERE username = ?");
						prep1.setString(1, newval);
						prep1.setString(2, userid);
						prep1.executeUpdate();
					}
					if(choice == 4)
					{	PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET landline = ? WHERE username = ?");
						prep1.setString(1, newval);
						prep1.setString(2, userid);
						prep1.executeUpdate();
					}if(choice == 5)
					{	PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET address = ? WHERE username = ?");
					prep1.setString(1, newval);
					prep1.setString(2, userid);
					prep1.executeUpdate();
				}
					if(choice == 6)
					{	PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET emailid = ? WHERE username = ?");
						prep1.setString(1, newval);
						prep1.setString(2, userid);
						prep1.executeUpdate();
					}
			}catch (Exception e) {

				e.printStackTrace();

			}
			}break;
			case 7: 
			{
				System.out.println("1) Debit card details\n\n 2) Credit Card Details\n\n");
				int choice1 = s.nextInt();
				switch(choice1)
				{
				case 1: 
				{
					s.nextLine();
					System.out.println("Enter Card number: ");
					String debitcardno = s.nextLine();
					System.out.println("Enter Expiry date: ");
					String debitcardexp = s.nextLine();
					System.out.println("Enter CVV: ");
					int debitcardcvv = s.nextInt();
					try
					{
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
						PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET debitcardno = AES_ENCRYPT(?,?), debitcardexp = ? , debitcardcvv = AES_ENCRYPT(?,?) WHERE username = ?");
						prep1.setString(1, debitcardno);
						prep1.setString(2, "key");
						prep1.setString(3, debitcardexp);
						prep1.setInt(4, debitcardcvv);
						prep1.setString(5, "key");		
						prep1.setString(6, userid);	
						prep1.executeUpdate();
					
					}catch (Exception e) {

						e.printStackTrace();

					}
				}break;
				case 2: 
				{
					s.nextLine();
					System.out.println("Enter Card number: ");
					String creditcardno = s.nextLine();
					System.out.println("Enter Expiry date: ");
					String creditcardexp = s.nextLine();
					System.out.println("Enter CVV: ");
					int creditcardcvv = s.nextInt();
					try
					{
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping","root", "root");
						PreparedStatement prep1 = con.prepareStatement("UPDATE userinfo SET debitcardno = AES_ENCRYPT(?,?), debitcardexp = ? , debitcardcvv = AES_ENCRYPT(?,?) WHERE username = ?");
						prep1.setString(1, creditcardno);
						prep1.setString(2, "key");
						prep1.setString(3, creditcardexp);
						prep1.setInt(4, creditcardcvv);
						prep1.setString(5, "key");	
						prep1.setString(6, userid);	
						prep1.executeUpdate();
					}catch (Exception e) {

					e.printStackTrace();

				}
				}break;
			}
			}break;
			
		 
		}
		
	}break;
		case 'n': case 'N':
		choices(userid); break;
		}
		for(int i = 0 ;i<100; i++)
			System.out.println();
			auth();
	
}
	}
	
	
	



