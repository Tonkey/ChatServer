/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient;

import java.util.List;
import java.util.Observer;

/**
 *
 * @author Michael
 */
public interface ObserveableInterface {
    
    public void addObserver(ObserverInterface o);
    public void removeObserver(ObserverInterface o);
    public void notifyObserver(String msg);
    public void notifyObserver(String[] userList);
    
    
}
