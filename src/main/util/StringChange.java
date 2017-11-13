package main.util;

public class StringChange {
    public static int INDEX_NOT_FOUND = Integer.MIN_VALUE;

    String originalString, newString;
    int index;
    String del, add;

    public StringChange(String original, String revised){
        this.originalString = original;
        this.newString = revised;
        this.del = "";
        this.add = "";
        this.index = INDEX_NOT_FOUND;

        this.getDiff();
    }

    private void getDiff(){
        this.getDiff(this.originalString, this.newString);
    }

    private void getDiff(String original, String revised){
        if(original.equals("")){
            this.index = 0;
            this.del = "";
            this.add = revised;
            return;
        }
        if(revised.equals("")){
            this.index = 0;
            this.del = revised;
            this.add = "";
            return;
        }
        if(original.equals(revised)){
            this.index = INDEX_NOT_FOUND;
            this.del = "";
            this.add = "";
            return;
        }

        //Iterate from the start of both strings until they no longer match
        int diffBeg;
        for (diffBeg = 0; diffBeg < original.length() && diffBeg < revised.length(); ++diffBeg) {
            if (original.charAt(diffBeg) != revised.charAt(diffBeg)) {
                break;
            }
        }

        //Iterate from the end of both strings until they no longet match
        int j;
        int diffEndOriginal = 0;
        int diffEndRevised = 0;
        for (j = 0; j < original.length() && j < revised.length(); ++j) {
            if (original.charAt(original.length()-1-j) != revised.charAt(revised.length()-1-j)) {
                break;
            }
            if(original.length()-j <= diffBeg || revised.length()-j <= diffBeg){
                break;
            }
        }

        //The diff is everything that was in the middle of the two indicies
        diffEndOriginal = original.length()-j;
        diffEndRevised = revised.length()-j;
        this.del = original.substring(diffBeg, diffEndOriginal);
        this.add = revised.substring(diffBeg, diffEndRevised);
        this.index = diffBeg;
    }

    public String toString(){
        String str = "";
        str += String.format("'%s' to '%s'", originalString, newString);
        if(this.index == INDEX_NOT_FOUND){
            str+= " :: NO CHANGE";
        }
        else{
            str += String.format(" :: at index %d DEL '%s' ADD '%s'", this.index, this.del, this.add);
        }
        return str;
    }

    public String getOriginalString() {
        return originalString;
    }

    public String getNewString() {
        return newString;
    }

    public String getDel() {
        return del;
    }

    public String getAdd() {
        return add;
    }

    public int getIndex(){
        return index;
    }
}
