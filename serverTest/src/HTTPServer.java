
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {

    private ServerSocket server;


    public HTTPServer(int port) throws IOException {
        server = new ServerSocket( port );
    }

    public void start() throws IOException {
        while (true) {
            Socket socket = server.accept();
            new Client( socket ).start();
        }
    }

    private class Client extends Thread {
        private Socket socket;

        public Client(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                acceptHTTP();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int contentLength = 0;

        private void acceptHTTP() throws IOException {
            /**
             * 接受HTTP请求
             */
            BufferedReader reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            String requestHeader;
            while ((requestHeader = reader.readLine()) != null && !requestHeader.isEmpty()) {
                parseHeader(requestHeader);
            }
            StringBuffer content = new StringBuffer();
            StringBuffer password = new StringBuffer();
            if (contentLength > 0) {

                for (int i = 0; i < contentLength; i++) {
                    content.append( (char) reader.read() );
                }
                System.out.println( "accept" + content.toString() );
           }
            writer();

        }


        private void parseHeader(String requestHeader) throws IOException {

            /**
             * 获得POST参数
             * 1.获取请求内容长度
             */
            System.out.println( requestHeader );
            String postHead = "Content-Length: ";
            if (requestHeader.startsWith( postHead )) {
                int begin = requestHeader.indexOf( postHead ) + postHead.length();
                String postParameterLength = requestHeader.substring( begin ).trim();
                contentLength = Integer.parseInt( postParameterLength );
                System.out.println( "POST参数长度是：" + Integer.parseInt( postParameterLength ) );
            }
        }

        private void writer() throws IOException {

            PrintWriter writer = new PrintWriter( socket.getOutputStream() );
            writer.println( "HTTP/1.1 200 OK" );
            writer.println( "Content-type: text/html" );
            writer.println();
            writer.println( "succeed！" );

            writer.flush();
        }

    }


    public static void main(String[] args) throws IOException {
        new HTTPServer( 8848 ).start();
//        try {
//            ServerSocket server = new ServerSocket( 8848 );
//
//            while (true) {
//                Socket socket = server.accept();
//                BufferedReader bd = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
//
//                /**
//                 * 接受HTTP请求
//                 */
//                String requestHeader;
//                int contentLength = 0;
//                while ((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty()) {
//                    System.out.println( requestHeader );
//
//                    /**
//                     * 获得POST参数
//                     * 1.获取请求内容长度
//                     */
//
//                    String postHead = "Content-Length: ";
//                    if (requestHeader.startsWith( postHead )) {
//                        int begin = requestHeader.indexOf( postHead ) + postHead.length();
//                        String postParameterLength = requestHeader.substring( begin ).trim();
//                        contentLength = Integer.parseInt( postParameterLength );
//                        System.out.println( "POST参数长度是：" + Integer.parseInt( postParameterLength ) );
//                    }
//                }
//                StringBuffer sb = new StringBuffer();
//                if (contentLength > 0) {
//                    for (int i = 0; i < contentLength; i++) {
//                        sb.append( (char) bd.read() );
//                    }
//                    System.out.println( "POST参数是：" + sb.toString() );
//                }
//
//                //发送回执
//                PrintWriter pw = new PrintWriter( socket.getOutputStream() );
//
//                pw.println( "HTTP/1.1 200 OK" );
//                pw.println( "Content-type:text/html" );
//                pw.println();
//                pw.println( "<h1>访问成功！</h1>" );
//
//                pw.flush();
//                socket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}