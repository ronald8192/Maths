import java.util.Scanner;

public class RSA {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		// Menu
		int select = 0;
		boolean sysCall = false;
		// Gen RSA key
		int prime1 = 0;
		int prime2 = 0;
		int pmax = 0; // max(prime1,prime2)
		int N = 0; // prime1 x prime2
		int TN = 0; // Euler Totient function
		int e = 0; // random interger, e>p1,p2
		int d = 0; // modular inverse of (e % N)

		// Encrypt
		char selectKey = ' ';
		int text = 0;
		int textEncrypted = 0;
		int custom_N = 0;
		int custom_e = 0;
		// Decrypt

		menu();
		do {
			if (!sysCall) {
				System.out.print("RSA>");
				select = input.nextInt();
			}
			sysCall = false;

			switch (select) {
			case 0:
				menu();
				break;
			case 1:
				// RSA keygen
				do {
					System.out.print("Enter prime number 1: ");
					prime1 = input.nextInt();
				} while (!isPrime(prime1));
				do {
					System.out.print("Enter prime number 2: ");
					prime2 = input.nextInt();
				} while (!isPrime(prime2));
				pmax = Math.max(prime1, prime2);
				N = prime1 * prime2;
				TN = mathsEulerTotient(prime1, prime2);
				do {
					System.out.print("e: (Enter 0 for random e)");
					e = input.nextInt();
					if (e == 0) {
						// random gen e
						do {
							e = (int) (Math.random() * 999999) + pmax;
						} while (mathsGCD(e, TN) != 1);
						break;
					} else if (mathsGCD(e, TN) != 1) {
						// user e, not co-prime
						System.out.println(e + "is not co-prime to" + TN);
						continue;
					} else {
						// user e, co-prime
						break;
					}
				} while (true);
				d = mathsModInverse(e, TN);
				// print key
				System.out.print("Public key (e,N):  ");
				System.out.println("(" + e + "," + N + ")");
				System.out.print("Private key (d,N): ");
				System.out.println("(" + d + "," + N + ")");

				break;
			case 2:
				// Encrypt
				custom_e = 0;
				custom_N = 0;
				if (prime1 == 0) {
					// no RSA key, call keygen
					do {
						System.out.print("Custom key / Generate new key ? (c,g): ");
						selectKey = input.next().toLowerCase().charAt(0);
					} while (!(selectKey == 'c' || selectKey == 'g'));
				} else {
					do {
						System.out.print("Custom key / Existing key / Generate new key ? (c,e,g): ");
						selectKey = input.next().toLowerCase().charAt(0);
					} while (!(selectKey == 'c'|| selectKey == 'g' || selectKey == 'e'));
				}
				if (selectKey == 'c') {
					// user input public key
					System.out.println("Enter public key (e,N): ");
					System.out.print("e: ");
					custom_e = input.nextInt();
					System.out.print("N: ");
					custom_N = input.nextInt();
				} else if (selectKey == 'e') {
					custom_e = e;
					custom_N = N;
				} else {
					// call keygen
					sysCall = true;
					select = 1;
					break;
				}
				
				// start encrypt
				System.out.print("Text to encrypt: ");
				text = input.nextInt();
				textEncrypted = (int)(Math.pow(text, custom_e) % custom_N);
				System.out.println("Encrypted text: " + textEncrypted);

				break;
			case 3:
				// Decrypt

				break;
			case 9:
				break;
			default:
				System.out.println("-RSA: " + select + ": Command not found");
				break;
			}

		} while (select != 9);
		System.out.println("Bye!");

	}

	private static void menu() {
		System.out.println("=======MENU=======");
		System.out.println("0. Print this menu");
		System.out.println("1. RSA keygen");
		System.out.println("2. Encrypt");
		System.out.println("3. Decrypt");
		System.out.println("\n9. Exit");
		System.out.println("=======MENU=======");
	}

	private static boolean isPrime(int num) {
		int numD3 = 0;

		if ((num & 1) == 0) {
			System.out.println(num + " is not prime.");
			return false;
		} else {
			numD3 = (int) Math.ceil(num / 3.0);
			for (int i = 3; i <= numD3; i++) {
				if (num % i == 0) {
					System.out.println(num + " is not prime.");
					return false;
				}
			}
			return true;
		}
	}

	private static int mathsEulerTotient(int num1, int num2) {
		return (num1 - 1) * (num2 - 1);
	}

	private static int mathsGCD(int a, int b) {
		if (a > b) {
			int swap = a;
			a = b;
			b = swap;
		}
		while (a != 0 && b != 0) {
			b %= a;
			int swap = a;
			a = b;
			b = swap;
		}
		// b is the GCD
		return b;
	}

	private static int mathsModInverse(int e, int N) {
		double k = 0;
		double x = 0;
		for (k = 0; k < (int) Double.MAX_VALUE; k++) {
			x = (1 + N * k) / e;
			if (x == (int) x) {
				return (int) x;
			}
		}
		return -1;
	}

}
