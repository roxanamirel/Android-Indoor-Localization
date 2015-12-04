package indoors.aalto.utils;

/**
 * Created by rroxa_000 on 11/30/2015.
 */
public enum DirPath {
    measurement_data, reference_data,
    A112, A118, A124, A128,A136, A141, A141_near("A141-near");

    private String path;
     DirPath(){}

     DirPath(String path){
         this.path = path;
     }

    public String getPath() {
        return path;
    }
}
