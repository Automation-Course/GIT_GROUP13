import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
				int base=base();
				String NumString;
				float ans=0;
				if(base==2) {
					NumString=Bin();

					if(NumString.charAt(0)=='0')
						ans=convPosiBin(convString(removeFirst(NumString),false));
					else
						ans=convNegBin(convString(removeFirst(NumString),true));

				}
				else {
					NumString=Dec();

					if(NumString.charAt(0)!='-')
						ans=convPosiDec(convString(NumString,false));
					else
						ans=convNegDec(convStringNegDec(removeFirst(NumString)));

				}

				System.out.println("the number is "+ans);
			}

			private static char[] convStringNegDec(String s) {

				char[] numArray = new char[s.length()];

				for (int i = 0; i <s.length() ; i++) {
					// Convert each character to integer and store in the array
					numArray[i] = s.charAt(i);
				}
				return numArray;
			}

			public static int base() {
				System.out.println("Please enter:\n"
						+ "2 to convert binary to decimal\n"
						+ "10 to convert decimal to binary");
				int base=0;
				try {
					base = sc.nextInt();
					if (base != 2 && base != 10) {
						System.out.println("Error: Input is not 2 or 10.");
						System.exit(0); // Exit the program since input is invalid
					}
				} catch (InputMismatchException e) {
					System.out.println("Error: Input is not a valid int.");
					System.exit(0); // Exit the program since input is invalid
				}
				return base;
			}

			public static String Bin() {
				System.out.println("Please enter number:\n"
						+ "for positive binary start with 0\n"
						+ "for negative binary start with 1");
				String numString = sc.next();

				//check value
				for (int i = 0; i < numString.length(); i++) {
					char digit = numString.charAt(i);
					int countDot=0;
					if (digit != '0' && digit != '1') {
						if(digit=='.') {
							if(countDot>0) {
								System.out.println("the number isn't Binary");
								System.exit(0); // Exit the program since input is invalid
							}
							else
								countDot++;
						}else {
							System.out.println("the number isn't Binary");
							System.exit(0); // Exit the program since input is invalid
						}
					}
				}
				return numString;
			}

			public static String Dec() {
				System.out.println("Please enter number:\n"
						+ "for negative Decimal start with -");
				String numString = sc.next();

				//check value
				for (int i = 0; i < numString.length(); i++) {
					char digit = numString.charAt(i);
					int countDot=0;
					if (digit < '0' && digit > '9') {
						if(digit=='.') {
							if(countDot>0) {
								System.out.println("the number isn't Decimal");
								System.exit(0); // Exit the program since input is invalid
							}
							else
								countDot++;
						}else
							if(digit=='-') {
								if(i!=0) {
									System.out.println("the number isn't Decimal");
									System.exit(0); // Exit the program since input is invalid
								}

							}
							else {
								System.out.println("the number isn't Decimal");
								System.exit(0); // Exit the program since input is invalid
							}
					}
				}
				return numString;

			}


			public static float convPosiBin(float num) {  //נכון רק לשלם
				//כפול 2 בחזקת מיקום
				int n=0,digit;
				long integer=(long) num;
				float copy=num;
				int fracPlaces = 0;
				while (copy != (long) copy) {
					copy *= 10;
					fracPlaces++;
				}
				float fraction=(float) (copy%Math.pow(10, fracPlaces)*Math.pow(10,-1*fracPlaces));

				float resInt=0;
				float resFrac=0;
				//integer
				while(integer>0) {
					resInt+=(integer%10)*Math.pow(2, n);
					n++;
					integer=integer/10;
				}
				//fraction
				n=-1;
				for(int i=0;i<fracPlaces;i++) {
					digit=(int) (fraction*10);
					resFrac+=((long)(digit))*Math.pow(2, n);
					n--;
					fraction=fraction*10%1;
				}

				return resInt+resFrac;

			}

			public static float convNegBin(float num) {
				//לשלוח להמרה חיובי בינארי לעשרוני
				num=convPosiBin(num);
				return (-1*num);

			}

			public static float convPosiDec(float num) {
				//חילוק
				long intNum=(long) num;
				num=num-intNum;

				float res=0;
				long resultInt,n=0;
				while (intNum!=0) {
					resultInt=intNum/2;
					if(resultInt*2!=intNum)
						res=(long) (res+1*Math.pow(10, n));
					n++;
					intNum=resultInt;
				}
				float resultFrac=0,full;
				for (int i=-1;i>-5;i--) {
					resultFrac=(float) (num*2);
					full = (int) resultFrac; // Should indicate the integer part of resultFrac
					num=resultFrac-full;
					res=res*10+full;
				}
				for(int i=-5;i<-1;i++) {
					res=res/10;
				}

				return res;

			}

			public static float convNegDec(char[] num) {
				//לשלוח להמרה עשרוני לבינארי חיובי
				float res;
				int size;
				res=convPosiDec(Float.parseFloat(new String(num)));
				if((long)res==1)
					size=sizeUntilDot(res)+1;
				else
					size=sizeUntilDot(res);

				//המרה משלילי לחיובי
				res=NegToPos(FloatToCharArray(res),size);
				if((long)res!=1) 
					res=(float) (res+Math.pow(10, size-1));

				return res;

			}

			public static int sizeUntilDot(float number) {
				String numberString = Float.toString(number);

				// Find the index of the decimal point
				int dotIndex = numberString.indexOf('.');

				// If dotIndex is -1, there is no decimal point
				if (dotIndex == -1) {
					// The whole number is the integer part
					return numberString.length();
				}

				// Return the number of characters before the decimal point
				return dotIndex;
			}

			public static float convString(String s,boolean binNeg) {
				String[] parts = s.split("\\.");
				String integerPart = parts[0];
				String fractionalPart="0";
				if (parts.length > 1) {
					fractionalPart = parts[1];
				}


				float integer, fractional=0;
				//integer part

				// if binary and negative change the number to positive by complete to 2
				if(binNeg) {
					int size=0;
					if (parts.length > 1) {
						size=integerPart.length()+fractionalPart.length()+1;
					}
					else {
						size=integerPart.length();
					}

					char[] numArray = new char[size];

					for (int i = 0; i <size ; i++) {
						// Convert each character to integer and store in the array
						numArray[i] = s.charAt(i);
					}
					integer=NegToPos(numArray,integerPart.length());
				}
				else
					integer = (float) Double.parseDouble(integerPart);


				// fraction part
				if (parts.length > 1)
					fractional = (float) (Float.parseFloat(fractionalPart)*Math.pow(10, -1*parts[1].length()));

				// Combine the integer and fractional parts
				float ans=integer+fractional;
				return ans;	

			}

			public static String removeFirst(String s) {

				return s.substring(1);

			}

			public static char[] FloatToCharArray(float number) {
				// Counting number of digits
				int digitCount = String.valueOf(number).length();

				// Creating an array to store the digits
				char[] digitsArray = new char[digitCount];

				// Extracting digits from the number and storing them in the array
				for (int i = 0; i < digitCount; i++) {
					digitsArray[i] = String.valueOf(number).charAt(i);
				}
				return digitsArray;

			}

			public static float NegToPos(char[] arrayNum,int sizeInt) {
				//היפוך ספרות
				for (int i = 0; i < arrayNum.length; i++) {

					if (arrayNum[i] == '0')
						arrayNum[i] = '1';
					else {
						if (arrayNum[i] == '1')
							arrayNum[i] = '0';
					}
				}

				//+1
				for (int j = 0; j < arrayNum.length; j++) {
					if (arrayNum[arrayNum.length-j-1] == '0') {
						arrayNum[arrayNum.length-j-1] = '1';

						return arrayCharToFloat(arrayNum);

					}
					else 
						if (arrayNum[arrayNum.length-j-1] == '1') 
							arrayNum[arrayNum.length-j-1] = '0';
				}

				return arrayCharToFloat(arrayNum);
			}

			public static float arrayCharToFloat(char[] arrayNum) {
				float res=0;
				boolean integer=true;
				int k=-1;
				for(int n=0;n<arrayNum.length;n++) {
					if(arrayNum[n]=='.')
						integer=false;
					else {
						if (integer) {
							res=res*10;
							res = res + (arrayNum[n] - '0'); // Convert character to numeric value by subtracting '0'
						}
						if(!integer) {
							res=(float) (res+(arrayNum[n] - '0')*Math.pow(10, k));
							k--;
						}
					}

				}
				return res;
			}
		}

	


