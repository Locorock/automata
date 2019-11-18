package trashBin;

public class stuffToRecycle {
       /*BROKEN


    public Path basicPath(Cell dest, WMap wMap){

        int count = 0;
        double sum = 0;
        Cell current = enviro.getGrid()[posx][posy];
        Path path = new Path (current);
        while(getNextCellTo (current, dest)!=dest){
            current = getNextCellTo (current, dest);
            path.getPath ().add(current);
            sum += wMap.getWeight(current.getX (),current.getY ());
            count++;
        }
        if(sum/count<2){
            return path;
        }else {
            return null;
        }
    }

    public Path advancedPathMemo(ArrayList<Cell> deadCells, Cell dest, WMap wMap, int posx, int posy, Path prevPath){
        if(prevPath.getLast ().equals (dest)){
            return prevPath;
        }else{
            Path nextPath = null;
            for(int i=-1;i<=1;i++) {
                for (int j = -1; j<=1; j++) {
                    if (!(i == 0 && j == 0) && Math.abs (i + j) == 1 && posx+j>=0 && posx+j<enviro.getWidth() && posy+i>=0 && posy+i<enviro.getWidth()) {
                        Cell c = enviro.getGrid ()[posx + j][posy + i];
                        if (!deadCells.contains(c) && !prevPath.contains (c) && (Math.abs (this.posx - posx) + Math.abs (this.posy - posy)) < range) {
                            double cost = wMap.getWeight (posx + j, posy + i);
                            Path cyclePath = advancedPathMemo (deadCells, dest, wMap, posx + j, posy + i, new Path (prevPath, c, cost));
                            if (nextPath == null || cyclePath!=null && cyclePath.getCost () < nextPath.getCost ()) {
                                nextPath = cyclePath;
                            }
                        }
                    }
                }
            }
            if(nextPath==null){
                deadCells.add(prevPath.getLast ());
            }
            return nextPath;
        }
    }


    public void generateWorldExp(int max, double temp, double hum, int alt){
        Enviro start = new Enviro(temp, alt, hum,"Plains", this, r);
        start.generate ();
        enviros.add(start);
        int offset = 0, toOffset = 1, added = 1, count = 0, test = 0;
        WorldGenSeries wseries = new WorldGenSeries();
        while(added!=0){
            added=0;
            for(int j=0, k=0;j<wseries.getSize();j++, k++){
                int repeat = wseries.get(j);
                Enviro a = enviros.get(k + offset);
                Enviro b = null;
                if(repeat>1) {
                    b = enviros.get((k + offset + 1));
                }
                else{
                    k--;
                }
                if((a==null || a.getDistance()>max)&& (b==null || b.getDistance()>max)){
                    enviros.add(null);
                }
                else{
                    Enviro c = new Enviro(0,0,0,"", this, r);
                    double newTemp;
                    double newHum;
                    double newAlt;
                    double newDist;
                    if((a!=null && a.getDistance()<max) && (b!=null && b.getDistance()<max)){
                        newTemp = (a.getTemperature()+b.getTemperature())/2;
                        newHum = (a.getHumidity()+b.getHumidity())/2;
                        newAlt = (a.getAltitude()+b.getAltitude())/2;
                        newDist = (a.getDistance()+b.getDistance())/2;
                    }else{
                        if(a==null || a.getDistance()>max){
                            a=b;
                            b=null;
                        }
                        newTemp = a.getTemperature();
                        newHum = a.getHumidity();
                        newAlt = a.getAltitude();
                        newDist = a.getDistance();
                    }
                    newHum = newHum+r.nextGaussian()*4;
                    if(newHum>100){ newHum=100;}  //DA NORMALIZZARE
                    if(newHum<0){ newHum=0;}

                    c.setDistance(newDist+1+((r.nextGaussian()-0.5)/1.8)); // 1.8 def
                    c.setTemperature(newTemp+r.nextGaussian()*4);  //VARIANZA STATISTICHE MONDO
                    c.setHumidity(newHum);
                    c.setAltitude((int)Math.round(newAlt+(r.nextGaussian()*3 -2)));

                    int quarter = ((j+count)/(wseries.getSize()/4))-1;
                    c.setEnviroByQuarter(a,b,quarter);
                    c.setBiome(assignBiome(c));
                    c.generate();
                    enviros.add(c);
                    test++;
                    System.out.println ("ENVIRO N: "+test);
                    added++;
                }
            }
            offset = toOffset;
            toOffset = toOffset+wseries.getSize();
            wseries.next();
            count++;
        }

        map = new ArrayList<ArrayList<Enviro>> ();
        mapWorld();

        generateRivers();
        printBiomes();
    } //DEPRECATED

    public void scanBranch(ArrayList<ArrayList<Enviro>> map, Enviro enviro, int x, int y){
        if(y>=map.size()){
            map.add(new ArrayList<Enviro> ());
        }
        if(x>=map.get(y).size()){
            for(int i=map.get(y).size();i<=x;i++){
                map.get(y).add(null);
            }
        }
        if(map.get(y).get(x)== null) {
            map.get(y).set(x,enviro);
            if(enviro.getEnviroRight()!=null)
                scanBranch(map, enviro.getEnviroRight(), x + 1, y);
            if(enviro.getEnviroLeft()!=null)
                scanBranch(map, enviro.getEnviroLeft(), x - 1, y);
            if(enviro.getEnviroDown()!=null)
                scanBranch(map, enviro.getEnviroDown(), x, y + 1);
            if(enviro.getEnviroUp()!=null)
                scanBranch(map, enviro.getEnviroUp(), x, y - 1);
        }
    }

    public void mapWorld(){ //
        for(int i=0;i<enviros.size();i++){
            if(enviros.get(i)==null){
                enviros.remove (i);
                i--;
            }
        }

        for(int i=0;i<200;i++){
            map.add(new ArrayList<> ());
            for(int j=0;j<200;j++){
                map.get(i).add(null);
            }
        }
        scanBranch(map,enviros.get(0),190,190);
        for(int i=0;i<map.size ();i++){
            boolean empty = true;
            for(int j=0;j<map.get(i).size();j++){
                if(map.get(i).get(j) != null){
                    empty = false;
                }
            }
            if(empty){
                map.remove (i);
                i--;
            }
        }
        int found = Integer.MAX_VALUE;
        for(int i=0;i<map.size ();i++) {
            for (int j = 0; j < map.get (i).size (); j++) {
                if (map.get (i).get (j) != null) {
                    if (j < found) {
                        found = j;
                    }
                }
            }
        }
        for(int j=0;j<found;j++){
            for(int i=0;i<map.size();i++){
                map.get(i).remove(0);
            }
        }

        int maxW = 0;
        for(int i=0;i<map.size();i++){
            if(map.get(i).size ()>maxW){
                maxW = map.get(i).size();
            }
        }

        for(int i=0;i<map.size();i++){
            for(int j=map.get(i).size();j<maxW;j++){
                map.get(i).add(null);
            }
        }
    } //DEPRECATED

    public void generateWorldAlt(int max, double temp, double hum, double rad, int seed){
        ArrayList<int[]> populated = new ArrayList<>();
        Enviro start = new Enviro(temp, rad, hum,"Plains",seed);
        enviros.add(start);
        int[] pos = {0,0};
        Random r = new Random(seed);
        start.setEnviroUp(generateNext(0, max, seed,0,0, temp+r.nextGaussian()*10,hum+r.nextGaussian()*10,rad+r.nextGaussian(), populated, new int[]{pos[0],pos[1]+1}, start));
        start.setEnviroRight(generateNext(0, max, seed,1,1, temp+r.nextGaussian()*10,hum+r.nextGaussian()*10,rad+r.nextGaussian(), populated, new int[]{pos[0]+1,pos[1]}, start));
        start.setEnviroDown(generateNext(0, max, seed,2,2, temp+r.nextGaussian()*10,hum+r.nextGaussian()*10,rad+r.nextGaussian(),populated, new int[]{pos[0],pos[1]-1}, start));
        start.setEnviroLeft(generateNext(0, max, seed,3,3, temp+r.nextGaussian()*10,hum+r.nextGaussian()*10,rad+r.nextGaussian(), populated, new int[]{pos[0]-1,pos[1]}, start));
        int[][] grid = new int[30][30];
        for(int i=0;i<30;i++) {
            Arrays.fill(grid[i],0);
        }
        for(int i=0;i<populated.size();i++){
            pos = populated.get(i);
            grid[pos[0]+15][pos[1]+15] = 1;
        }
        for(int i=0;i<30;i++){
            for(int j=0;j<30;j++){
                System.out.print(grid[j][i]);
            }
            System.out.println();
        }
    }

    BROKEN
    public Enviro generateNext(double dist,int max, int seed, int dir, int lastdir, double temperature, double humidity, double radiation, ArrayList<int[]> pop, int[] pos, Enviro prev){
        for(int[] p : pop){
            if(p[0]==pos[0] && p[1]==pos[1]){
                //DA PROGRAMMARE ALLACCIAMENTO CON COORDINATE E TOGLIERE COORDINATE TAROCCHE
                return null;
            }
        }
        Random r = new Random(seed);
        double newdist;
        if(dir!=lastdir){
            newdist=dist+0.7+r.nextGaussian()/20*0.7;
        }else {
            newdist = dist+1+r.nextGaussian()/20;
        }
        if(dist>max){
            return null;
        }else{
            pop.add(pos);
            Enviro current = new Enviro(temperature, radiation, humidity, "",seed);
            switch(dir) {
                case 0: {
                    current.setEnviroDown(prev);
                }
                case 1: {
                    current.setEnviroLeft(prev);
                }
                case 2: {
                    current.setEnviroUp(prev);
                }
                case 3: {
                    current.setEnviroRight(prev);
                }
            }

            int startPoint = r.nextInt(4);
            for(int i=0;i<4;i++){
                int point = (i+startPoint)%4;
                switch(point) {
                    case 0: {
                        current.setEnviroUp(generateNext(newdist, max, seed,0,dir, temperature+r.nextGaussian()*10,humidity+r.nextGaussian()*10,radiation+r.nextGaussian(), pop, new int[]{pos[0],pos[1]+1}, current));
                    }
                    case 1: {
                        current.setEnviroRight(generateNext(newdist, max, seed,1,dir, temperature+r.nextGaussian()*10,humidity+r.nextGaussian()*10,radiation+r.nextGaussian(), pop, new int[]{pos[0]+1,pos[1]}, current));
                    }
                    case 2: {
                        current.setEnviroDown(generateNext(newdist, max, seed,2,dir, temperature+r.nextGaussian()*10,humidity+r.nextGaussian()*10,radiation+r.nextGaussian(),pop, new int[]{pos[0],pos[1]-1}, current));
                    }
                    case 3: {
                        current.setEnviroLeft(generateNext(newdist, max, seed,3,dir, temperature+r.nextGaussian()*10,humidity+r.nextGaussian()*10,radiation+r.nextGaussian(), pop, new int[]{pos[0]-1,pos[1]}, current));
                    }
                }
            }
            enviros.add(current);
            return current;
        }


    }

        public void setEnviroByQuarter(Enviro toLink1, Enviro toLink2, int quarter){
        if(quarter == -1){ quarter = 3;}
        switch(quarter){
            case 0:
                this.setEnviroLeft(toLink1);
                this.setEnviroDown(toLink2);
                toLink1.setEnviroRight(this);
                if(toLink2!=null)
                    toLink2.setEnviroUp(this);
                break;
            case 1:
                this.setEnviroUp(toLink1);
                this.setEnviroLeft(toLink2);
                toLink1.setEnviroDown(this);
                if(toLink2!=null)
                    toLink2.setEnviroRight(this);
                break;
            case 2:
                this.setEnviroRight(toLink1);
                this.setEnviroUp(toLink2);
                toLink1.setEnviroLeft(this);
                if(toLink2!=null)
                    toLink2.setEnviroDown(this);
                break;
            case 3:
                this.setEnviroDown(toLink1);
                this.setEnviroRight(toLink2);
                toLink1.setEnviroUp(this);
                if(toLink2!=null)
                    toLink2.setEnviroLeft(  this);
                break;

        }
    }






    */

}
