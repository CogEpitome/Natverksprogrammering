package clientserver.client.view;

/**
 *
 * @author Jonas
 * 
 * Provides synchronized console output functions.
 */
class SyncedOutput {
    synchronized void print(String out){
        System.out.print(out);
    }
    synchronized void println(String out){
        System.out.println(out);
    }   
}
