package com.chao.dcme.ot;

import java.util.ArrayList;
import java.util.List;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-16 15:08.
 * Package: com.chao.dcme.ot
 * Description: functions of inclusion transformation
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ITransformation {
    // apply inclusion transformation i1 against i2
    public static Insertion ii(Insertion i1, Insertion i2) {
        int pa = i1.getPos(), pb = i2.getPos();
        if (pa < pb) {
            // if pos of insertion i2 is after pos of insertion i1, it will have no influence
            // to i1, so simply return i1;
            return i1;
        }
        // if pos of insertion i2 is before to pos of insertion i1, it will have
        // influence on i1. Since we want to know i1 against i2, so i2 executed earlier than
        // i1. So when pos of i2 == pos of i1, we still need to consider the i2's effect
        return new Insertion(i1.getStr(), pa + i2.getStr().length());
    }

    public static Insertion id(Insertion a, Deletion b) {
        int pa = a.getPos(), pb = b.getPos(), dlen = b.getDlen();
        if (pa <= pb) {
            // the pos of previous deletion is equal or greater than the pos of insertion,
            // so that operation won't have any influence on operation a. Simply return a;
            return a;
        } else if (pa > pb + dlen) {
            // there are dlen characters before insertion pos which have been deleted. So
            // simply modify the index of insertion dlen before
            return new Insertion(a.getStr(), pa - dlen);
        }
        // the pos of insertion locates in the range of deletion, so simply start from
        // pos of deletion
        Insertion iret = new Insertion(a.getStr(), pb);
        saveLI(iret, a, b);
        return iret;
    }

    public static List<Deletion> di(Deletion a, Insertion b) {
        int pa = a.getPos(), pb = b.getPos(), dlen = a.getDlen();
        List<Deletion> list = new ArrayList<Deletion>();
        if (pb >= pa + dlen) {
            // the pos of insertion is greater or equal to the pos of deletion + dlen, which
            // means this insertion won't have any influence on deletion
            list.add(a);
        } else if (pa >= pb) {
            // insertion adds several current characters before the start pos of deletion,
            // so simply move the start position right N characters
            list.add(new Deletion(dlen, pa + b.getStr().length()));
        } else {
            // By the rule, we shouldn't delete the characters which is inserted before, so we have
            // to divide the deletion operation into two parts.
            list.add(new Deletion(pb - pa, pa));
            list.add(new Deletion(dlen - (pb - pa), pb + b.getStr().length()));
        }
        return list;
    }

    public static Deletion dd(Deletion a, Deletion b) {
        int pa = a.getPos(), pb = b.getPos(), dlen = a.getDlen();
        Deletion iret = null;
        if (pb >= pa + a.getDlen()) {
            // the pos of prev deletion greater than the current deletion range,
            // no influence, simply return original operation
            iret = a;
        } else if (pa >= pb + b.getDlen()) {
            // the pos of prev deletion smaller than the current deletion range,
            // simply move original operation N characters left
            iret = new Deletion(dlen, pa - b.getDlen());
        } else if (pb <= pa && pa + dlen <= pb + b.getDlen()) {
            // pb's deletion range covers pa's deletion range
            iret = new Deletion(0, pa);
        } else if (pb <= pa && pa + dlen > pb + b.getDlen()) {
            // a's rightside deletion range is greater than pb, so just
            // start from pb and delete those more characters
            iret = new Deletion(pa + dlen - (pb + b.getDlen()), pb);
        } else if (pb > pa && pb + b.getDlen() >= pa + dlen) {
            // a's start pos is smaller than b but b's rightside deletion range is
            // greater than a, so simply delete the characters between pa and pb
            iret = new Deletion(pb - pa, pa);
        } else {
            // start from a and delete the character's which is not contained in b
            // since a's range covers b's range
            iret = new Deletion(dlen - b.getDlen(), pa);
        }
        saveLI(ire, a, b);
        return iret;
    }

}
