package org.elsys.ip;

import static org.elsys.kstoimenov.PrimeNumber.isPrime;

class WayTooBigException extends Exception {
    WayTooBigException() {
    }
}

class NotAnIntegerException extends Exception {
    NotAnIntegerException() {
    }
}

public class Main {
    public static void main(String[] args) {
        for (String token:args) {
            try {
                if (token.contains(".")) {
                    throw new NotAnIntegerException();
                }

                double currentNum = Double.parseDouble(token);

                if (currentNum < Integer.MIN_VALUE || currentNum > Integer.MAX_VALUE) {
                    throw new WayTooBigException();
                }

                if ((int)(currentNum) != currentNum) {
                    throw new NotAnIntegerException();
                }

                if (isPrime((int) currentNum)) {
                    System.out.printf("%s is a prime\n", token);
                } else {
                    System.out.printf("%s is not a prime\n", token);
                }
            } catch (NumberFormatException ignored) {
                System.out.printf("%s is not a number\n", token);
            } catch (WayTooBigException e) {
                System.out.printf("%s is out of bound\n", token);
            } catch (NotAnIntegerException e) {
                System.out.printf("%s is not an integer\n", token);
            }
        }
    }
}
