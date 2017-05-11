package message;

import gui.Manager;
import gui.mainPage.MainPage;

public class LogoutType extends ReactMessage {
    LogoutType(MainPage mainPage, Message message) {
        super(mainPage, message);
    }

    @Override
    public void react() {
        String[] parameters = message.getHeader().split(" ");
        System.out.println(parameters.length);
        for( String str : parameters )
            System.out.println(str);
        if( parameters.length != 1 )
            return ;

        if( Manager.wantToClose ) {
            System.out.println("stage close");
            Manager.Stage.close();
            return;
        }

        System.out.println("change to login");
        try {
            Manager.changeToLogin();
        } catch (Exception e) {
            System.exit(-1);
        }
    }
}
