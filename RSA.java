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
		String selectKey = "";
		int text = 0;
		double textEncrypted = 0L;
		// Decrypt

		menu();
		do {
			if (!sysCall) {
				System.out.print("RSA>");
				select = input.nextInt();
			}

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
				do {
					System.out.print("Use public key (" + e + "," + N + ") ? (y,n)");
					selectKey = input.next();
				} while (selectKey == "y" || selectKey == "n");
				if (selectKey == "n" && prime1 == 0) {
					// no RSA key, call keygen
					System.out.println("Calling keygen...");
					select = 1;
					break;
				} else {
					// use generated key
					// start encrypt
					System.out.print("Text to encrypt: ");
					text = input.nextInt();
					textEncrypted = Math.pow(text, e) % N;
					System.out.println("Encrypted text: " + textEncrypted);
				}
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
