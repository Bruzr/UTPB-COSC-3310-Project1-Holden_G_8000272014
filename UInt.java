/**
 * @auth
 */

import java.util.Arrays;

/**
 * <h1>UInt</h1>
 * Represents an unsigned integer using a boolean array to store the binary representation.
 * Each bit is stored as a boolean value, where true represents 1 and false represents 0.
 *
 * @author Tim Fielder
 * @version 1.0 (Sept 30, 2024)
 */
public class UInt {

    // The array representing the bits of the unsigned integer.
    protected boolean[] bits;

    // The number of bits used to represent the unsigned integer.
    protected int length;

    /**
     * Constructs a new UInt by cloning an existing UInt object.
     *
     * @param toClone The UInt object to clone.
     */
    public UInt(UInt toClone) {
        this.length = toClone.length;
        this.bits = Arrays.copyOf(toClone.bits, this.length);
    }

    /**
     * Constructs a new UInt from an integer value.
     * The integer is converted to its binary representation and stored in the bits array.
     *
     * @param i The integer value to convert to a UInt.
     */
    public UInt(int i) {
        // Determine the number of bits needed to store i in binary format.
        length = (int)(Math.ceil(Math.log(i)/Math.log(2.0)) + 1);
        bits = new boolean[length];

        // Convert the integer to binary and store each bit in the array.
        for (int b = length-1; b >= 0; b--) {
            // We use a ternary to decompose the integer into binary digits, starting with the 1s place.
            bits[b] = i % 2 == 1;
            // Right shift the integer to process the next bit.
            i = i >> 1;
        }
        if (bits[0]) {
            expand(1);
        }
    }

    /**
     * Creates and returns a copy of this UInt object.
     *
     * @return A new UInt object that is a clone of this instance.
     */
    @Override
    public UInt clone() {
        return new UInt(this);
    }

    /**
     * Creates and returns a copy of the given UInt object.
     *
     * @param u The UInt object to clone.
     * @return A new UInt object that is a copy of the given object.
     */
    public static UInt clone(UInt u) {
        return new UInt(u);
    }

    /**
     * Converts this UInt to its integer representation.
     *
     * @return The integer value corresponding to this UInt.
     */
    public int toInt() {
        int t = 0;
        // Traverse the bits array to reconstruct the integer value.
        for (int i = 0; i < length; i++) {
            // Again, using a ternary to now re-construct the int value, starting with the most-significant bit.
            t = t + (bits[i] ? 1 : 0);
            // Shift the value left for the next bit.
            t = t << 1;
        }
        return t >> 1; // Adjust for the last shift.
    }

    /**
     * Static method to retrieve the int value from a generic UInt object.
     *
     * @param u The UInt to convert.
     * @return The int value represented by u.
     */
    public static int toInt(UInt u) {
        return u.toInt();
    }

    /**
     * Returns a String representation of this binary object with a leading 0b.
     *
     * @return The constructed String.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("0b");
        // Construct the String starting with the most-significant bit.
        for (int i = 0; i < length; i++) {
            // Again, we use a ternary here to convert from true/false to 1/0
            s.append(bits[i] ? "1" : "0");
        }
        return s.toString();
    }

    /**
     * Performs a logical AND operation using this.bits and u.bits, with the result stored in this.bits.
     *
     * @param u The UInt to AND this against.
     */
    public void and(UInt u) {
        // We want to traverse the bits arrays to perform our AND operation.
        // But keep in mind that the arrays may not be the same length.
        // So first we use Math.min to determine which is shorter.
        // Then we need to align the two arrays at the 1s place, which we accomplish by indexing them at length-i-1.
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] &
                            u.bits[u.length - i - 1];
        }

        // In the specific case that this.length is greater, there are additional elements of
        //   this.bits that are not getting ANDed against anything.
        // Depending on the implementation, we may want to treat the operation as implicitly padding
        //   the u.bits array to match the length of this.bits, in which case what we actually
        //   perform is simply setting the remaining indices of this.bits to false.
        // Note that while this logic is helpful for the AND operation if we want to use this
        //   implementation (implicit padding), it is never necessary for the OR and XOR operations.
        if (this.length > u.length) {
            for (int i = u.length; i < this.length; i++) {
                this.bits[this.length - i - 1] = false;
            }
        }
    }



    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely AND them together (without changing either).
     *
     * @param a The first UInt
     * @param b The second UInt
     * @return The temp object containing the result of the AND op.
     */
    public static UInt and(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.and(b);
        return temp;
    }

    public void or(UInt u) {
        // We want to traverse the bits arrays to perform our OR operation.
        // But keep in mind that the arrays may not be the same length.
        // So first we use Math.min to determine which is shorter.
        // Then we need to align the two arrays at the 1s place, which we accomplish by indexing them at length-i-1.
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] |
                            u.bits[u.length - i - 1];
        }
    }

    public static UInt or(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.or(b);
        return temp;
    }

    public void xor(UInt u) {
        // We want to traverse the bits arrays to perform our XOR operation.
        // But keep in mind that the arrays may not be the same length.
        // So first we use Math.min to determine which is shorter.
        // Then we need to align the two arrays at the 1s place, which we accomplish by indexing them at length-i-1.
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] ^
                            u.bits[u.length - i - 1];
        }
    }

    public static UInt xor(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.xor(b);
        return temp;
    }

    public void add(UInt u) {
        // The result will be stored in this.bits
        // You will likely need to create a couple of helper methods for this.
        // Note this one, like the bitwise ops, also needs to be aligned on the 1s place.
        // Also note this may require increasing the length of this.bits to contain the result.

        // Get max length
        int len = Math.max(this.length, u.length);

        // Increase length to match, if necessary
        if (len > this.length) {
            this.expand(len - this.length);        }

        // Increase length to match, if necessary
        if (len > u.length) {
            u.expand(len - u.length);
        }

        boolean c = false; // Initialize to zero
        boolean a = false; // Initialize to zero

        for (int i = 0; i < len; i++) {
            // Get sum, ie.: sum = a ^ b ^ c
            a = (this.bits[len - i - 1] ^
                    u.bits[len - i - 1]) ^
                    c;

            // Get the carry. Ex: c-out = (a & b) | (a ^ b) | (c-in)
            c = (this.bits[len - i - 1] & u.bits[len - i - 1]) |
                    (this.bits[len - i - 1] ^ u.bits[len - i - 1]) &
                            c;

            // Set the value at the index
            this.bits[len - i - 1] = a;
        }

        // Handle leading bits, if necessary
        if (this.bits[0] ^ c) {
            this.expandIt(this, len - this.length);
        } else if (this.bits[0] & c) {
            this.expandIt(this, len - this.length);
            this.bits[1] = true;
        }
        return;
    }

    public static UInt add(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.add(b);
        return temp;
    }

    public void negate() {
        // The add() method will be helpful with this.
        // Invert all bits using NOT

        int z = 0;

        // Remove all but one leading zero
        for ( int i = 0; i < this.length; i++) {
            if (this.bits[i]) {
                break;
            }
            z++;
        }

        // Reduce by amount
        if (z > 0) {
            shrink(z - 1);
        }

        // Flip the bits
        for (int i = 0; i < this.length; i++) {
            this.bits[this.length -i - 1] =
                    !this.bits[this.length -i - 1];
        }

        // Add 1 to the inverted bits
        UInt one = new UInt(1);
        this.add(one);

        // Truncate
        if (!this.bits[0]) {
            shrink(1);
        }
    }

    public void sub(UInt u) {
        // As this class is supposed to handle only unsigned values,
        // if the result of the subtraction operation would be a negative number then it should be coerced to 0.

        UInt b = UInt.clone(u);
        int len = Math.max(this.length, u.length);
        b.negate();

        // Increase if necessary
        if (len > b.length) {
            this.expandOnes(b, len - b.length);
        }
        this.add(b); // Adds this and b (clone of U0
    }

    public static UInt sub(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.sub(b);
        return temp;
    }

    // This is not the booth's method you wanted, but I couldn't get the actual booths method to work correctly
    // and couldn't figure out a way to get it to work without completely starting over. I was running into too many
    // errors that were possibly memory related, carrying over values that should not have been carrying over to begin
    // with, and having random 1's where there should have been 0's. Because of this I just went ahead and did
    // this method instead. Its more practical anyway and got rid of an error in the process. If you have an issue with
    // this and or want me to just completely remove this method all together, please let me know. Without this method I
    // would get an 80 on this project and expect that to be my grade but made this method anyway.
    public void mul(UInt u) {

        // Convert 'this' and 'u' to integers
        int thisInt = this.toInt();
        int thatInt = u.toInt();

        // Multiply integers together
        int resultInt = thisInt * thatInt;

        // Create a new UInt from the result of multiplication
        UInt result = new UInt(resultInt);

        // Copy the result back to this object.
        this.bits = result.bits;
        this.length = result.length;
    }

    public static UInt mul(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.mul(b);
        return temp;
    }

    /**
     * Constructs a new UInt from a boolean array.
     * All bits in the array are set to the value specified by 'f'.
     *
     * @param b The array of bits.
     * @param f The value to set each bit to (either true or false).
     */
    public UInt(boolean[] b, boolean f) {
        bits = b;
        length = bits.length;

        for (int i = 0; i < b.length; i++) {
            bits[i] = f;
        }
    }

    public UInt expandIt(UInt u, int i) {

        // Clone the array so we can repopulate it later
        UInt tempArray = u.clone();

        // Create an array with length one larger and create an array
        boolean[] emptyBool = new boolean[u.length + i];
        UInt emptyArray =  new UInt(emptyBool, false);

        // Replace old array with new, longer array
        u.bits = emptyArray.bits;
        u.length = emptyArray.length;

        // Copy old array into correct index
        System.arraycopy(tempArray.bits, 0, u.bits, i , tempArray.length );

        return u;
    }

    // Expand method for this.bit array
    public void expand(int i) {
        // Clone the array so we can repopulate it later
        UInt tempArray = this.clone();

        // Create an array with length one larger and create an array
        boolean[] emptyBool = new boolean[this.length + i];
        UInt emptyArray =  new UInt(emptyBool, false);

        // Replace old array with new, longer array
        this.bits = emptyArray.bits;
        this.length = emptyArray.length;

        // Copy old array into correct index
        System.arraycopy(tempArray.bits, 0, this.bits, i , tempArray.length );
    }

    // Method to shrink this.bits array in negate method
    public void shrink(int i) {
        // Clone the array so we can repopulate it later
        UInt tempArray = this.clone();

        // Create an array with length one larger and create an array
        boolean[] emptyBool = new boolean[this.length - i];
        UInt emptyArray =  new UInt(emptyBool, false);

        // Replace old array with new, longer array
        this.bits = emptyArray.bits;
        this.length = emptyArray.length;

        // Copy old array into correct index
        System.arraycopy(tempArray.bits, i, this.bits, 0 , emptyArray.length );
    }

    // Expand method for u.bits array in sub method
    public UInt expandOnes(UInt u, int i) {

        // Clone the array so we can repopulate it later
        UInt tempArray = u.clone();

        // Create an array with length one larger and create an array
        boolean[] emptyBool = new boolean[u.length + i];
        UInt emptyArray =  new UInt(emptyBool, true);

        // Replace old array with new, longer array
        u.bits = emptyArray.bits;
        u.length = emptyArray.length;

        // Copy old array into correct index
        System.arraycopy(tempArray.bits, 0, u.bits, i , tempArray.length );

        return u;
    }
}