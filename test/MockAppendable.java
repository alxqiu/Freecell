import java.io.IOException;

/**
 * Mock class that implements Appendable. Created with a number to designate number
 * of characters that can be appended to it without throwing an exception.
 *
 * <p> Contains a charLimit integer that represents the number of characters that can be
 * appended to it until it throws an IOException, and a CharSequence curr that
 * represents all characters appended to this object. </p>
 *
 * <p>
 *   Method behavior, including which object each Append returns a reference to, is
 *   intended to behave as closely to the Javadoc documentation of {@link Appendable}
 *   as possible.
 * </p>
 */
public class MockAppendable implements Appendable {
  private int charLimit;
  private CharSequence curr;

  /**
   * Constructs a new TestInvalidAppendable object with an empty CharSequence field,
   * and sets the charLimit to the given value.
   * @param charLimit integer limit to characters that can be appended.
   */
  public MockAppendable(int charLimit) {
    this(charLimit, "");
  }

  /**
   * Constructs new TestInvalidAppendable object with given charLimit and
   * curr for the respective fields.
   *
   * @param charLimit number of characters that can be appended until IOException
   * @param curr current characters to create object with
   * @throws IllegalArgumentException when givne null
   */
  public MockAppendable(int charLimit, CharSequence curr)
          throws IllegalArgumentException {
    if (curr == null) {
      throw new IllegalArgumentException("given null arg");
    }

    this.charLimit = charLimit;
    this.curr = curr;
  }

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    // will throw IOException when csq is empty String and charLimit == 0
    // csq.length() doesn't throw negative, so we can expect that an exception is thrown
    // whenever charLimit is zero or charLimit - csq.length() goes anywhere lower than 1.
    if (charLimit - csq.length() <= 0) {
      throw new IOException();
    }
    charLimit -= csq.length();
    curr = curr.toString() + csq;
    return this;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    if (charLimit - (end - start) <= 0) {
      throw new IOException();
    }
    charLimit -= (end - start);
    curr = curr.toString() + csq.subSequence(start, end);
    return this;
  }

  @Override
  public Appendable append(char c) throws IOException {
    if (charLimit - 1 <= 0) {
      throw new IOException();
    }
    charLimit--;
    curr = curr.toString() + c;
    return this;
  }

  @Override
  public String toString() {
    return curr.toString();
  }
}
