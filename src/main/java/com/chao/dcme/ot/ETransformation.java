package com.chao.dcme.ot;

import java.util.ArrayList;
import java.util.List;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-16 15:10.
 * Package: com.chao.dcme.ot
 * Description: functions of exclusion transformation
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ETransformation {
    public static Insertion ii(Insertion a, Insertion b) {
        int pa = a.getPos(), pb = b.getPos();
        Insertion iret = null;
        if (pa <= pb) {
            // the insertion pos of b is greater than a, so remove it from
            // context won't have any influence on a
            iret = a;
        } else if (pa >= pb + b.getStr().length()) {
            // the insertion pos of a is greater than b+len, so remove b from
            // context will cause a's start pos shift left N characters
            iret = new Insertion(a.getStr(), pa - b.getStr().length());
        } else {
            // the insertion pos of a is between the range of b, so removing b
            // will result in a's operation range undefined. So we use a relative
            // addressing method here, and let it be dealt with by higher level
            iret = new Insertion(a.getStr(), pa - pb);
            saveRA(iret, b);
        }
        return iret;
    }

    public static Insertion id(Insertion a, Deletion b) {
        // check whether ob was involved in an information-losing inclusion transformation
        // which resulted in a
        if (checkLI(a, b)) {
            return recoverLI(a);
        }
        int pa = a.getPos(), pb = b.getPos();
        if (pa <= pb) {
            // b's start pos after a, no influence
            return a;
        }
        // b's start pos before a, so simply add dlen into a's start pos to remove the effect of b
        return new Insertion(a.getStr(), pa + b.getDlen());
    }

    public static List<Deletion> di(Deletion a, Insertion b) {
        List<Deletion> list = new ArrayList<Deletion>();
        int pa = a.getPos(), pb = b.getPos(), dlen = a.getDlen(), lb = b.getStr().length();
        if (pa + dlen <= pb) {
            // insertion's start pos greater than deletion, so no influence on deletion
            list.add(a);
        } else if (pa >= pb + lb) {
            // pos of deletion is greater than pb+lb, so simply shift start pos lb characters left
            list.add(new Deletion(a.getDlen(), pa - lb));
        } else {
            if (pb <= pa && pa + dlen <= pb + lb) {
                // relative address pa- pb, since b's range covers a's range
                list.add(new Deletion(dlen, pa - pb));
            } else if (pb <= pa && pa + dlen > pb + lb) {
                // save relative address, delete the first pb+lb-pa characters in insertion str
                list.add(new Deletion(pb + lb - pa, pa - pb));
                // remove the characters outside the range of insertion
                list.add(new Deletion(pa + dlen - (pb + lb), pb));
            } else if (pa < pb && pb + lb <= pa + dlen) {
                // a's range covers b's range, so first delete all the characters inserted by b
                // 0 is a relative address with b
                list.add(new Deletion(lb, 0));
                // delete all the characters outside insertion scope
                list.add(new Deletion(dlen - lb, pa));
            } else {
                list.add(new Deletion(pa + dlen - pb, 0));
                list.add(new Deletion(pb - pa, pa));
            }
            saveRA(list, b);
        }
        return list;
    }

    public static List<Deletion> dd(Deletion a, Deletion b) {
        if (checkLI(a, b)) {
            return recoverLI(a);
        }
        List<Deletion> list = new ArrayList<Deletion>();
        int pa = a.getPos(), pb = b.getPos(), la = a.getDlen(), lb = b.getDlen();
        if (pb >= pa + la) {
            // deletion b's pos is greater than pa+la, so no influence
            list.add(a);
        } else if (pa >= pb) {
            // start pos of a is greater than b, so simply shift start pos of a
            // lb characters right, since it's previously deleted by b
            list.add(new Deletion(la, pa + lb));
        } else {
            // overlap range, divide it into two operations
            list.add(new Deletion(pb - pa, pa));
            list.add(new Deletion(la - (pb - pa), pb + lb));
        }
        return list;

    }
}
