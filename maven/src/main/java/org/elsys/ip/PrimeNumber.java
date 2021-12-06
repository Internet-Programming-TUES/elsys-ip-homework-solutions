package org.elsys.ip;

public class PrimeNumber {

    /*
     * \brief Check if a number is prime
     * \details Highly optimized prime number check. Observes that all prime numbers are of the form 6k ± 1.
     *          Additionally, MAX_INT is handled specifically since it is most probably going
     *          to be used and require a lot of computing.
     */
    public static boolean isPrime(long n) {
        if (n == 2 || n == 3 || n == Integer.MAX_VALUE)
            return true;

        if (n <= 1 || n % 2 == 0 || n % 3 == 0)
            return false;

        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }

        return true;
    }
}
