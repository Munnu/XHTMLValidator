import java.io.*;
import java.net.*;
import java.util.Stack;

/**
 *  Program to validate an XHTML page
 * informs the user if their xhtml page validates
 * as program runs it specifies where the tag mismatch occurs
 *  
 *  @author  Monique Blake
 *  @version February 25, 2011
 */
public class ValidateXHTML {
  /** Reads tokens from the input */
  public static void main(String[] args) throws IOException {
    BufferedReader input;
    StreamTokenizer tokens;
    int previousToken;
    
    // Stores html tags in a stack
    Stack<String> htmlTagStack = new Stack<String>();
    
    if (args.length == 0) {
      // read from input
      input = new BufferedReader(new InputStreamReader(System.in));
    } 
    else {
      // read from URL
      URL url = new URL(args[0]);
      input = 
        new BufferedReader(new InputStreamReader(url.openStream()));
    }
    tokens = new StreamTokenizer(input);
    tokens.ordinaryChar('/');
    tokens.ordinaryChar('"');
    tokens.ordinaryChar('\'');
    
    previousToken = -1;
    
    while(tokens.nextToken() != StreamTokenizer.TT_EOF ) {
      
      // Checks to see if the object inputted contains a < and a word      
      if(tokens.ttype == 60){
        previousToken = tokens.ttype;
        tokens.nextToken();
        
        //checks to see if the next character is an ! and skips it
        if(tokens.ttype == 33){
          System.out.println("Skipped Comment");
        }
        
        // if next char is a / which indicates a closing </
        if(tokens.ttype == 47) {
          previousToken = tokens.ttype;
          tokens.nextToken();
          
          // if current string token looks just like the one in the stack
          if(tokens.sval.equals(htmlTagStack.peek())){
            
            System.out.println("Popped <"+ htmlTagStack.peek() + "> found a match");
            htmlTagStack.pop();  
            previousToken = tokens.ttype;
            tokens.nextToken();
          } else {
            // if not, push this closing tag inside stack
            System.out.println("Mismatched tag, current line in file is: </"+ tokens.sval +
                               "> but, top of stack contains <" + htmlTagStack.peek() + ">");
            previousToken = tokens.ttype;
            tokens.nextToken();
           }
        }
        // if the next character is a word                                                                                                                                                               
        if(tokens.ttype == StreamTokenizer.TT_WORD) {
          // pushes word inside                                                                                                                                                                          
          htmlTagStack.push(tokens.sval);
          System.out.println("Pushed <" + htmlTagStack.peek()+ ">");

          // if next thing read is an >
          while(tokens.ttype != 62){
            previousToken = tokens.ttype;
            tokens.nextToken();
          }
          if(previousToken == 47) {
               System.out.println("Self closing tag, popped: <"+htmlTagStack.peek()+"/>");
              htmlTagStack.pop();
            }
          }
      }
      
      
    }
    
    if(htmlTagStack.empty()){
      if (tokens.nextToken() == StreamTokenizer.TT_EOF) {
        System.out.println("File Validates!");
      }
    }
    
    
  }
}



