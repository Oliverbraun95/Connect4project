import javax.swing.*;
import java.awt.*;

public class Connect4 {

    public Connect4() {
        JFrame frame = new JFrame("DrawGrid");
        frame.setSize(700, 700);
        frame.setPreferredSize(frame.getSize());
        frame.add(new MultiDraw(frame.getSize()));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... argv) {
        new Connect4();
    }

    public static class MultiDraw extends JPanel {
        int X = 25;
        int Y = 25;
        int cellSize = 50;
        int turn = 2;
        int rows = 6;
        int cols = 7;
        int colX = 40;
        boolean winner=false;
        boolean possibleMove=true;
        String ccolor = "";

        // Spielfeldeinstellung
        Color[][] grid = new Color[rows][cols];
        public MultiDraw(Dimension dimension) {
            setLayout(null);
            for (int col = 0; col < grid[0].length; col++) {
                JButton button = new JButton(String.valueOf(col+1));
                add(button);
                button.setBorderPainted(false);
                button.setBackground(new Color(255, 255, 255));
                button.setFocusPainted(false);
                button.setBounds(colX,Y,cellSize+5,cellSize+5);
                button.addActionListener(e -> {
                    // wenn kein Gewinner, weiterspielen
                    if(!winner){
                        int Xpos = button.getX()-40;
                        int clickedRow;
                        int clickedCol = Xpos/(cellSize+10);
                        clickedRow = dropP(clickedCol);

                        if(clickedRow!=-1) {

                            if (turn % 2 == 0) {
                                grid[clickedRow][clickedCol] = Color.red;
                                ccolor = "RED";
                            } else {
                                grid[clickedRow][clickedCol] = Color.yellow;
                                ccolor = "Yellow";
                            }
                            turn++;
                            // wenn Gewinner, keine weiteren Spielschritte erlauben
                            if (checkForWinner(clickedCol, clickedRow, grid[clickedRow][clickedCol])) {
                                winner = true;

                            }
                            if (checkForDraw(clickedCol, clickedRow, grid[clickedRow][clickedCol])) {
                                possibleMove = false;
                            }
                        }
                        repaint();
                    }
                });
            // Abstand zwischen Spalten
                colX += cellSize+10;
            }
            setSize(dimension);
            setPreferredSize(dimension);
        // Neustartbutton
            JButton restartButton = new JButton("Restart");
            add(restartButton);
            restartButton.setBorderPainted(false);
            restartButton.setBackground(new Color(255,255,255));
            restartButton.setFocusPainted(false);
        // Ändere Location statt x 550 nehmen
            restartButton.setBounds(550,100,100,cellSize);
            restartButton.addActionListener(e -> reset());
            int x = 0;
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    Color c;
                    if(x%2==0){
                        grid[row][col] = Color.white;
                    }else{
                        grid[row][col] = Color.white;
                    }
                    x++;

                }

            }
            // Exitbutton
            // Ändere Location statt x 550 nehme Anzahl ColX + Abstand Anzahl + Abstand
            //x: int cols* int cellSize + 100 - get Methode da nicht public? colX += cellSize+10
            JButton exitButton = new JButton("Exit");
            add(exitButton);
            exitButton.setBorderPainted(false);
            exitButton.setBackground(new Color(255,255,255));
            exitButton.setFocusPainted(false);
            exitButton.setBounds(550,200,100,cellSize);
            exitButton.addActionListener(e -> {
                System.exit(0);
            });

            // Hilfeanzeigen
            JButton helpButton = new JButton("Help");
            add(helpButton);
            helpButton.setBorderPainted(false);
            helpButton.setBackground(new Color(255,255,255));
            helpButton.setFocusPainted(false);
            helpButton.setBounds(550,300,100,cellSize);
            helpButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Textplatzhalter für Spielregeln");
            });


        }

        @Override
        // Größe und Farbe Grafik g1 Hintergrund, g2 Blaues Spielbrett
        public void paintComponent(Graphics g) {
            X = 40;
            Y = 100;
            Graphics2D g1 = (Graphics2D)g;
            Dimension d = getSize();
            g1.setColor(new Color(1, 1, 1));
            g1.fillRect(0,0,d.width,d.height);
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(new Color(0, 0, 255));
            g2.fillRect(X - 15,Y - 15,grid[0].length*(cellSize+10) + 20,grid.length*(cellSize+10) + 20);

            //2) draw grid here, Runde Zellen Voreinstellung
            for (Color[] colors : grid) {
                for (int col = 0; col < grid[0].length; col++) {

                    g2.setColor(colors[col]);
                    g2.fillArc(X, Y, cellSize, cellSize, 0, 360);
                    g2.setColor(Color.black);
                    g2.drawArc(X, Y, cellSize, cellSize, 0, 360);
                    X += cellSize + 10;
                }
                Y += cellSize + 10;
                X = 40;
            }

    // Hier sollte Text in ein Label gepackt werden od zumindest Position auch relativ gemacht werden
            g2.setColor(new Color(255, 255, 255));
            if(!winner){
                // Unentschieden
                if(!possibleMove){
                    g2.drawString("DRAW",X + 500, 20);
                }
                // weiterer Zug
                else{
                    if(turn%2==0) {
                        g2.setColor(new Color(255, 0, 0));
                        g2.drawString("Red's Turn", X + 500, 40);
                    }
                    else {
                        g2.setColor(new Color(245, 220, 8));
                        g2.drawString("Yellow's Turn", X + 500, 40);
                    }
                }
            // Gewinner
            }else{
                g2.drawString("WINNER - "+ ccolor,X + 500,20);
            }

        }

        // Was bedeutet dropP?
        public int dropP(int cc){
            int cr = grid.length-1;

            while(cr>=0){

                if(grid[cr][cc].equals(Color.white)){
                    return cr;
                }
                cr--;
            }

            return -1;

        }
// Prüfe auf Unentschieden
        private boolean checkForDraw(int clickedCol, int clickedRow, Color color) {
            for (Color[] colors : grid) {
                for (int col = 0; col < grid[0].length; col++) {
                    if (colors[col].equals(Color.WHITE)) {
                        return false;

                    }
                }
            }
            return true;
        }

// Prüfe auf Gewinner
        public boolean  checkForWinner(int cc,int cr, Color c) {
            //search west and east
            int xStart = cc;
            int count = 1;
            //check west
            xStart--;
            while(xStart>=0){
                if(grid[cr][xStart].equals(c)){
                    count++;
                }else{
                    break;
                }
                if(count==4)
                    return true;

                xStart--;
            }

            //check east
            xStart = cc;
            xStart++;
            while(xStart<grid[0].length){

                if(grid[cr][xStart].equals(c)){

                    count++;
                }else{
                    break;
                }
                if(count==4)
                    return true;

                xStart++;
            }

            /*
             * More searches here
             */

            //check North
            count = 1;
            int yStart = cr;
            yStart--;
            while(yStart>0){
                if(grid[yStart][cc].equals(c)){
                    count++;
                }else{
                    break;
                }
                if(count==4)
                    return true;

                yStart--;
            }

            //check east
            yStart = cr;
            yStart++;
            while(yStart<grid.length){

                if(grid[yStart][cc].equals(c)){

                    count++;
                }else{
                    break;
                }
                if(count==4)
                    return true;

                yStart++;
            }
            /*
             * More Searches
             */

            //check NorthWest
            count = 1;
            yStart = cr;
            xStart = cc;
            xStart--;
            yStart--;
            while(yStart>0 && xStart>0){
                if(grid[yStart][xStart].equals(c)){
                    count++;
                }else{
                    break;
                }
                if(count==4)
                    return true;

                yStart--;
                xStart--;
            }

            //check Southeast
            yStart = cr;
            yStart++;
            xStart = cc;
            xStart++;
            while(yStart<grid.length && xStart<grid.length){

                if(grid[yStart][xStart].equals(c)){

                    count++;
                }else{
                    break;
                }
                if(count==4)
                    return true;

                yStart++;
                xStart++;
            }

            /*
             * More Searches
             */

            //check southWest
            count = 1;
            yStart = cr;
            xStart = cc;
            xStart--;
            yStart++;
            while(yStart<grid.length && xStart>0){
                if(grid[yStart][xStart].equals(c)){
                    count++;
                }else{
                    break;
                }
                if(count==4)
                    return true;

                yStart++;
                xStart--;
            }

            //check Northeast
            yStart = cr;
            yStart--;
            xStart = cc;
            xStart++;
            while(yStart>0 && xStart<grid.length){

                if(grid[yStart][xStart].equals(c)){

                    count++;
                }else{
                    break;
                }
                if(count==4)
                    return true;

                yStart--;
                xStart++;
            }

            return false;
        }
// Reset?
        public void reset(){
            winner=false;
            possibleMove=true;
            turn=2;
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    grid[row][col] = Color.white;

                }
            }
            repaint();
        }

    }
}
