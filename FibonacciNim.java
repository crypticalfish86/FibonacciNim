/*
This program simulates a maths game called "Fibonacci Nim", the rules of the game are as below:
    1. A certain number of coin heaps are set up (set up for 3, but you can change this)
    2. Each coin heap begins with the same amount of coins (set up for 13, but you can change this)
    3. 2 players take turns taking a certain number of coins from coin heaps.
    4. On the first turn the coin heap is taken from they can take at most all but one of the coins
    5. On every respective turn they can take at most twice as many coins that were taken before
    6. The player who takes the last coin from all the heaps wins the game
*/

import java.util.Scanner;

public class FibonacciNim
{
    static final int STARTING_HEAP_NUMBER = 3;
    static final int STARTING_NUMBER_OF_COINS_PER_HEAP = 13;
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        /*Setting up initial number of coin heaps, starting coins and available coins
        in each heap to arrays (so user integer input can be directly used to call a heap)*/
        int[] coinHeaps = new int[STARTING_HEAP_NUMBER];
        int[] coinHeapAvailableCoins = new int[STARTING_HEAP_NUMBER];
        for (int i = 0; i < STARTING_HEAP_NUMBER; i++)
        {
            coinHeaps[i] = STARTING_NUMBER_OF_COINS_PER_HEAP;
            coinHeapAvailableCoins[i] = STARTING_NUMBER_OF_COINS_PER_HEAP - 1;
        }

        /*Setting up players (only one turn bool needed because it flips to false signifying
        player two's turn and back to true to signify player ones turn)*/
        boolean playerOneTurn = true;
        boolean playerOneSkip = true;
        boolean playerTwoSkip = true;

        /*game starts, initial check to see if heaps have any coins this is checked again at
         the end of every single iteration of the while loop which simulates a player round*/
        boolean heapsStillHaveCoins = checkHeapsStillHaveCoins(coinHeaps);
        while (heapsStillHaveCoins)
        {
            /*This first do-while loop has 3 sub-blocks of code associated with determining
            the heap a player chooses to take coins from (or reset)*/
            int playerHeapInput;
            boolean isHeapInputLegalInteger = false;
            printRemainingCoins(coinHeaps);
            do
            {
                //Determine player turn to give commands to right player and determine player skip
                int playerTurn = playerOneTurn? 1 : 2;
                boolean playerAllowedSkip =
                        (playerOneSkip && playerTurn == 1) || (playerTwoSkip && playerTurn ==2);
                String heapChoiceCommand = String.format("Player %d: choose a heap: ", playerTurn);
                System.out.print(heapChoiceCommand);

                //Check for non integer input, readdressing the player until integer is inputted
                while (!input.hasNextInt())
                {
                    System.out.println("Sorry you must enter an integer in the range -3 to 3, "
                            + "excluding zero.");
                    input.nextLine();
                    System.out.print(heapChoiceCommand);
                }
                playerHeapInput = input.nextInt();
                input.nextLine(); //clear any previous input

                /*Determine actions from integer input, whether its resetting heap, allowing
                  the player to go forward to the coin taking loop or addressing illegal int inputs
                  like taking coins from a heap that doesn't exist or from a heap with no coins.*/
                if (playerHeapInput > 0 && playerHeapInput <= STARTING_HEAP_NUMBER
                        && coinHeaps[playerHeapInput - 1] != 0)
                {
                    isHeapInputLegalInteger = true;
                }
                else if (playerHeapInput >= -STARTING_HEAP_NUMBER && playerHeapInput < 0)
                {
                    if (playerAllowedSkip)
                    {
                        //Resets Heap and flips player skip to false depending on who's turn it is
                        coinHeaps[-playerHeapInput - 1] = STARTING_NUMBER_OF_COINS_PER_HEAP;
                        coinHeapAvailableCoins[-playerHeapInput - 1] =
                                STARTING_NUMBER_OF_COINS_PER_HEAP - 1;
                        String heapReset =
                                String.format("Heap %d has been reset", -playerHeapInput);
                        System.out.println(heapReset);
                        printRemainingCoins(coinHeaps);
                        playerOneSkip = playerOneTurn? false : playerOneSkip;
                        playerTwoSkip = playerOneTurn? playerTwoSkip : false;
                        playerOneTurn = !playerOneTurn;
                    }
                    else
                    {
                        System.out.println("Sorry you have used your reset.");
                        printRemainingCoins(coinHeaps);
                    }
                }
                else
                {
                    System.out.println("Sorry that's not a legal heap choice.");
                }
            }while (!isHeapInputLegalInteger);

            /*Now that we have a legal heap we can code taking coins from it, this do-while
             loop has 3 sub-blocks of code, dealing with taking coins from heaps*/
            int coinsTaken;
            boolean isCoinInputLegalInteger = false;
            do
            {
                //Instruct the player to take coins based on available coins for that heap
                String coinsAvailableInformation =
                        String.format("Now choose a number of coins between 1 and %d: ",
                                coinHeapAvailableCoins[playerHeapInput - 1]);
                System.out.print(coinsAvailableInformation);

                //Check for non integer input, readdressing the player until integer is inputted
                while (!input.hasNextInt())
                {
                    System.out.println("Sorry you must enter an integer.");
                    input.nextLine(); //clear input so player can try again
                    String coinsAvailableReprompt =
                            String.format("Now choose between 1 and %d: ",
                                    coinHeapAvailableCoins[playerHeapInput - 1]);
                    System.out.print(coinsAvailableReprompt);
                }
                coinsTaken = input.nextInt();
                input.nextLine(); //clear any previous input that may affect future inputs

                /*Check the integer input is between 1 and the max amount of coins available
                  for that heap before updating that heap, the while loops bool not being
                  fulfilled until an acceptable integer is inputted.*/
                if (coinsTaken < 1 || coinsTaken > coinHeapAvailableCoins[playerHeapInput - 1])
                {
                    System.out.println("Sorry that's not a legal number of coins for that heap.");
                }
                else
                {
                    coinHeaps[playerHeapInput - 1] -= coinsTaken;
                    coinHeapAvailableCoins[playerHeapInput - 1] =
                            updateAvailableCoins(coinsTaken, coinHeaps[playerHeapInput - 1]);
                    isCoinInputLegalInteger = true;
                }
            }while (!isCoinInputLegalInteger);

            /*player ends turn and next player turn begins(current bool value gets flipped).
            Unless coin heaps are empty and while loop breaks allowing winner to be
            calculated based on current player turn*/
            heapsStillHaveCoins = checkHeapsStillHaveCoins(coinHeaps);
            if (heapsStillHaveCoins)
            {
                playerOneTurn = !playerOneTurn;
            }
        }
        //while loop broken therefore coin heaps must be empty and winner gets declared
        if (playerOneTurn)
        {
            System.out.println("Player 1 wins!");
        }
        else
        {
            System.out.println("Player 2 wins!");
        }
    }

    /*method checks if coins are still in the heap, once it detects a coin is in the heap
    bool flips to true and the for loop ends*/
    static boolean checkHeapsStillHaveCoins(int[] coinHeaps)
    {
        for (int i = 0; i < STARTING_HEAP_NUMBER; i++)
        {
            if (coinHeaps[i] > 0)
            {
                return true;
            }
        }
        return false;
    }

    /*method to print the number of coins in each heap left, loop needed due to variable
    length of array*/
    static void printRemainingCoins(int[] coinHeaps)
    {
        System.out.print("Remaining coins: ");
        for (int i = 0; i < STARTING_HEAP_NUMBER; i++)
        {
            String remainingCoinsInCurrentHeap = String.format("%d", coinHeaps[i]);
            System.out.print(remainingCoinsInCurrentHeap);
            if (i != STARTING_HEAP_NUMBER - 1)
            {
                System.out.print(", ");
            }
            else
            {
                System.out.print("\n"); //last iteration needs a new-line, not a comma.
            }
        }
    }

    /*if the remaining coins in the coin heap are less than twice of what was taken,
    update available coins for that index to just be the coins left in heap,
    otherwise update it to be up to twice of what was just taken from heap*/
    static int updateAvailableCoins(int coinsTaken, int coinHeap)
    {
        if (coinsTaken * 2 > coinHeap)
        {
            return coinHeap;
        }
        else
        {
            return coinsTaken * 2;
        }
    }
}
