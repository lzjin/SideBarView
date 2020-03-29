package com.lzj.sidebarviewdemo.utils;


import com.lzj.sidebarviewdemo.bean.SortBean;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Created by lzj on 2018/6/11.
 * 根据某字段排序
 */

public class SortComparator implements Comparator {
    private int compareCallNum = 0;

    @Override
    public int compare(Object o1, Object o2) {
        compareCallNum = 0;
        return compareString(((SortBean) o1).getName(), ((SortBean) o2).getName());
    }

    private int compareString(String o1, String o2) {
        compareCallNum++;
        //若存在长度为0的情况：
        if ((o1.length() == 0) && (o2.length() == 0)) {
            return 0;
        } else if (o1.length() == 0) {
            return -1;
        } else if (o2.length() == 0) {
            return 1;
        }

        String firstStrA = o1.substring(0, 1);
        String firstStrB = o2.substring(0, 1);

        int typeA = getFirstCharType(o1);
        int typeB = getFirstCharType(o2);

        if (typeA > typeB) {
            return -1;//返回负值，则往前排
        } else if (typeA < typeB) {
            return 1;
        }

        //类型相同，需要进行进一步的比较
        int compareResult;

        if (typeA < 9 && typeB < 9) {
            compareResult = firstStrA.compareTo(firstStrB);
            if (compareResult != 0) {
                //若不同，立即出来比较结果
                return compareResult;
            } else {
                //若相同，则递归调用
                return compareString(o1.substring(1), o2.substring(1));
            }
        }

        //是字母或汉字

        //若是首字母，先用第一个字母或拼音进行比较
        //否则，先判断字符类型
        String firstPinyinA = PinYinStringHelper.getFirstPingYin(o1).substring(0, 1);
        String firstPinyinB = PinYinStringHelper.getFirstPingYin(o2).substring(0, 1);
        if (compareCallNum == 1) {
            compareResult = firstPinyinA.compareTo(firstPinyinB);
            if (compareResult != 0) {
                return compareResult;
            }
        }
        //若首字的第一个字母相同，或不是首字，判断原字符是汉字还是字母，汉字排在前面
        typeA = getFirstCharType2(o1);
        typeB = getFirstCharType2(o2);
        if (typeA > typeB) {
            return -1;
        } else if (typeA < typeB) {
            return 1;
        }

        //不是首字母，在字符类型之后判断，第一个字母或拼音进行比较
        if (compareCallNum != 1) {
            compareResult = firstPinyinA.compareTo(firstPinyinB);
            if (compareResult != 0) {
                return compareResult;
            }
        }

        if (isLetter(o1) && isLetter(o2)) {
            //若是同一个字母，还要比较下大小写
            compareResult = firstStrA.compareTo(firstStrB);
            if (compareResult != 0) {
                return compareResult;
            }
        }

        if (isHanzi(o1) && isHanzi(o2)) {
            //使用姓的拼音进行比较
//            compareResult = firstPinyinA.compareTo(firstPinyinB);
            compareResult = PinYinStringHelper.getFirstPingYin(o1)
                    .compareTo(PinYinStringHelper.getFirstPingYin(o2));
            if (compareResult != 0) {
                return compareResult;
            }

            //若姓的拼音相同，比较汉字是否相同
            compareResult = firstStrA.compareTo(firstStrB);
            if (compareResult != 0) {
                return compareResult;
            }
        }
        //若相同，则进行下一个字符的比较（递归调用）
        return compareString(o1.substring(1), o2.substring(1));
    }

    private int getFirstCharType2(String str) {
        if (isHanzi(str)) {
            return 10;
        } else if (isLetter(str)) {
            return 9;
        } else if (isNumber(str)) {
            return 8;
        } else {
            return 0;
        }
    }

    private int getFirstCharType(String str) {
        if (isHanzi(str)) {
            return 10;
        } else if (isLetter(str)) {
            return 10;
        } else if (isNumber(str)) {
            return 8;
        } else {
            return 0;
        }
    }

    private boolean isLetter(String str) {
        char c = str.charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        return pattern.matcher(c + "").matches();
    }

    private boolean isNumber(String str) {
        char c = str.charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[1-9]+$");
        return pattern.matcher(c + "").matches();
    }

    private boolean isHanzi(String str) {
        char c = str.charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5]+");
        return pattern.matcher(c + "").matches();
    }
}
