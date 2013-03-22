package com.igrs.tivic.phone.Utils;
/**
 * Utility methods frequently used by data classes and design-time
 * classes.
 */
public final class ModelUtil {
	
	public static boolean isUsrNameValid(String name)
	{
		if(name != null)
		{
			return true;
		}
		
		return false;
	}
	
    public static final boolean areEqual(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        else if (o1 == null || o2 == null) {
            return false;
        }
        else {
            return o1.equals(o2);
        }
    }

    
    public static final boolean areBooleansEqual(Boolean b1, Boolean b2) {
        // !jwetherb treat NULL the same as Boolean.FALSE
        return (b1 == Boolean.TRUE && b2 == Boolean.TRUE) ||
                (b1 != Boolean.TRUE && b2 != Boolean.TRUE);
    }

   
    public static final boolean areDifferent(Object o1, Object o2) {
        return !areEqual(o1, o2);
    }

    public static final boolean areBooleansDifferent(Boolean b1, Boolean b2) {
        return !areBooleansEqual(b1, b2);
    }


    public static final boolean hasNonNullElement(Object[] array) {
        if (array != null) {
            final int n = array.length;
            for (int i = 0; i < n; i++) {
                if (array[i] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final String concat(String[] strs) {
        return concat(strs, " ");  //NOTRANS
    }

    public static final String concat(String[] strs, String delim) {
        if (strs != null) {
            final StringBuffer buf = new StringBuffer();
            final int n = strs.length;
            for (int i = 0; i < n; i++) {
                final String str = strs[i];
                if (str != null) {
                    buf.append(str).append(delim);
                }
            }
            final int length = buf.length();
            if (length > 0) {
                //  Trim trailing space.
                buf.setLength(length - 1);
            }
            return buf.toString();
        }
        else {
            return ""; // NOTRANS
        }
    }

    public static final boolean hasLength(String s) {
        return (s != null && s.length() > 0);
    }
    
    /**
	 * 判断给定字符串是否空白串。
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty( String input ) 
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}

    public static final boolean hasTrimmedLength(String s) {
        return (s != null && s.trim().length() > 0);
    }

    public static final boolean isTrue(String s) {
        return (hasLength(s) && s.equals("true"));
    }

    public static final String nullifyIfEmpty(String s) {
        return ModelUtil.hasLength(s) ? s : null;
    }

    public static final String nullifyingToString(Object o) {
        return o != null ? nullifyIfEmpty(o.toString()) : null;
    }
    
    public static final String emptyStringIfNull(String string){
        if(!hasLength(string)){
            return "";
        }
        return string;
    }

    public static boolean hasStringChanged(String oldString, String newString) {
        if (oldString == null && newString == null) {
            return false;
        }
        else if ((oldString == null && newString != null)
                || (oldString != null && newString == null)) {
            return true;
        }
        else {
            return !oldString.equals(newString);
        }
    }
}
