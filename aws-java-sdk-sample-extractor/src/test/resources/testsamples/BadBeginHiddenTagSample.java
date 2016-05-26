package com.amazonaws.codesamples.test;

public class BadBeginHiddenTagSample {

    public void sample() {
        //BEGIN_SAMPLE:Test.Sample
        //DESCRIPTION:A sample
        String foo = "foo!";
        //BEGIN_HIDDEN
        //BEGIN_HIDDEN
        String bar = "bar!";
        //END_HIDDEN
        //END_SAMPLE
    }

}
