package com.paranoidtimes.mailboxtaskexecutor.imap;

import com.paranoidtimes.mailboxtaskexecutor.api.EmailHandler;
import java.io.PrintStream;
import java.util.Enumeration;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.internet.MimeMultipart;

/**
 * An example
 * <pre>EmailHandler</pre> implementation that prints the e-mail to specified
 * <pre>PrintStream</pre>.
 *
 * @author djosifovic
 */
public class PrintingEmailHandler implements EmailHandler {

    /**
     * The
     * <pre>PringStream</pre> to print e-mails to.
     */
    private final PrintStream printStream;

    /**
     * A message header delimiter text.
     */
    private static final String MESSAGE_HEADERS_DELIMETER = "=== Message Headers ===";

    /**
     * A message content delimeter text.
     */
    private static final String MESSAGE_CONTENT_DELIMETER = "=== Message Content ===";

    /**
     * A message start delimeter text.
     */
    private static final String MESSAGE_START_DELIMETER = "=== Message Start ===";

    /**
     * A message end delimeter text.
     */
    private static final String MESSAGE_END_DELIMETER = "=== Message End ===";

    /**
     * A message content part delimeter text.
     */
    private static final String MESSAGE_CONTENT_PART_DELIMETER = "=== Message Content Part ===";

    /**
     * A default constructor.
     *
     * @param printStream stream to print e-mail to.
     */
    public PrintingEmailHandler(PrintStream printStream) {
        this.printStream = printStream;
    }

    /**
     * {@inheritDoc}
     *
     * Prints e-mail headers, that prints the content. If the content is
     * multipart, print every part.
     *
     * @param message message to handle/print.
     * @throws Exception if message can not be printed to the stream.
     */
    @Override
    public void handleEmail(Message message) throws Exception {
        print(MESSAGE_START_DELIMETER);
        Enumeration allHeaders = message.getAllHeaders();
        printMessageHeaders(allHeaders);
        printMessageContent(message.getContent());
        print(MESSAGE_END_DELIMETER);
    }

    /**
     * Prints all message headers in the format &lt;header name&gt;:&lt;header
     * value&gt;.
     *
     * @param headers headers to print.
     * @throws Exception if headers can not be printed.
     */
    private void printMessageHeaders(Enumeration headers) throws Exception {
        print(MESSAGE_HEADERS_DELIMETER);
        while (headers.hasMoreElements()) {
            Header h = (Header) headers.nextElement();
            print(h.getName() + ":" + h.getValue());
        }
    }

    /**
     * Print the message content. Print the delimeter and calls
     * <pre>doPrintMessageContent</pre>.
     *
     * @param content content to print.
     * @throws Exception if the content can not be printed.
     */
    private void printMessageContent(Object content) throws Exception {
        print(MESSAGE_CONTENT_DELIMETER);
        doPrintMessageContent(content);
    }

    /**
     * Handles only
     * <pre>MimeMultipart</pre> and
     * <pre>String</pre> parts. If the object is multipart, traverses all parts
     * and print them. If the object is a string, just prints that string. All
     * other possible types of the content object are not specially handled,
     * just called
     * <pre>toString</pre> on them.
     *
     * @param contentObject object to print.
     * @throws Exception if the content object can not be printed.
     */
    private void doPrintMessageContent(Object contentObject) throws Exception {
        if (contentObject instanceof MimeMultipart) {
            MimeMultipart mm = (MimeMultipart) contentObject;
            for (int i = 0; i < mm.getCount(); i++) {
                doPrintMessageContent(mm.getBodyPart(i).getContent());
            }
        } else if (contentObject instanceof String) {
            print(MESSAGE_CONTENT_PART_DELIMETER);
            print((String) contentObject);
        } else {
            print(MESSAGE_CONTENT_PART_DELIMETER);
            print(contentObject.toString());
        }
    }

    /**
     * Prints the passed text to the
     * <pre>PrintStream</pre>.
     *
     * @param text text to print.
     * @throws Exception if the passed text can not be printed.
     */
    private void print(String text) throws Exception {
        printStream.println(text);
    }
}