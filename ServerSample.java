public class ServerSample{

    public static void main(String args[]){
        // initialize
        LineSocketServer server = new LineSocketServer(8082);
        
        final LineSocketServer that_server = server;
        server.addEventHandler(new LineSocketServerEventHandler(){
                public void onMessage(int client_id, String line){
                    System.out.println("* <"+client_id+"> "+ line);
                    that_server.getClient(client_id).send("echo : <"+client_id+"> "+line);
                    
                }
                public void onAccept(int client_id){
                    System.out.println("* <"+client_id+"> connection accepted");
                }
                public void onClose(int client_id){
                    System.out.println("* <"+client_id+"> closed");
                }
            });
        
        server.listen();
        
        new Thread(){
            public void run(){
                while(true){
                    try{
                        String msg = new java.util.Date().toString();
                        System.out.println("<broadcast> "+msg);
                        for(LineSocket sock : that_server.getClients()){
                            if(sock.send(msg)) Thread.sleep(10);
                        }
                        Thread.sleep(10000);
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }
}