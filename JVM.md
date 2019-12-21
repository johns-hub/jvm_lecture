#   JVM

## 类的加载、连接与初始化

- 类加载

  - 在Java代码中，类型的加载、连接与初始化过程都是在程序运行期间完成的

  - 提供了更大的灵活性，增加了更多的可能性

  - 类加载器深入剖析
    - Java虚拟机与程序的生命周期
    - 在如下几种情况下，Java虚拟机将结束生命周期
      - 执行了System.exit()方法
      - 程序正常执行结束
      - 程序在执行过程中遇到了异常或错误而异常终止
      - 由于操作系统出现错误而导致Java虚拟机进程终止



- 类的加载、连接与初始化

  - 加载：查找并加载类的二进制数据
  - 连接
    - 验证：确保被加载的类的正确性
    - 准备：为类的静态变量分配内存，并将其初始化为默认值
    - 解析：把类中的符号引用转换为直接引用

  - 初始化：为类的静态变量赋予正确的初始值

![](https://i.postimg.cc/z3YggVXj/th-id-OIP.jpg)

- 类的使用与卸载

  - 使用

  - 卸载


  - Java程序对类的使用方式分为两种

    - 主动使用

    - 被动使用

  - 所有的Java虚拟机实现必须在每个类或接口被Java程序”首次主动使用“时才初始化它们


  - 主动使用（七种）
    - 创建类的实例
    - 访问某个类或接口的静态变量，或者对该静态变量赋值
    - 调用类的静态方法
    - 反射（如Class.forName("com.test.Test")）
    - 初始化一个类的子类
    - Java虚拟机启动时被标明为启动类的类（java Test）
    - JDK1.7开始提供的动态语言支持：java.lang.invoke.MethodHandle实例的解析结果REF_getStatic，REF_putStatic，REF_invokeStatic句柄对应的类没有初始化，则初始化
    - 除了以上七种情况，其他使用Java类的方式都被看作是对类的被动使用，都不会导致类的初始化


### 加载

- 类的加载指的是将类的.class文件中的二进制数据读入到内存中，将其放到运行时数据区的方法区，然后在内存中创建一个java.lang.Class对象（规范并未说明Class对象位于哪里，HotSpot虚拟机将其放在了方法区中）用来封装类在方法区的数据结构

- 加载.class文件的方式

  - 在本地系统中直接加载

  - 通过网络下载.class文件

  - 从zip，jar等归档文件中加载.class文件

  - 从专有数据库中提取.class文件

  - 将Java源文件动态编译为.class文件


  ![](https://i.postimg.cc/jqWLRd3w/1575904204x3661913030.png)



```
加载：就是把二进制形式的java类型读入java虚拟机中
验证：
	准备：为类变量分配内存，设置默认值。但是在到达初始化之前，类变量都没有初始化为真正的初始值
	解析：解析过程是在类型的常量池中寻找类、接口、字段和方法的符号引用，把这些符号引用替换成直接引用的过程
初始化：为类变量赋予正确的初始值

类实例化：
为新的对象分配内存
为实例变量赋默认值
为实例变量赋正确的初始值
java编译器为它编译的每一个类都至少生成一个实例初始化
方法，在java的class文件中，这个实例初始化方法被称为
"<init>”。针对源代码中每一个类的构造方法，java编译器都
生成一个<init>方法
```

![](https://i.postimg.cc/nzdktczc/1575904565x3703728804.png)



- 类的加载的最终产品是位于内存中的Class对象

- Class对象封装了类在方法区内的数据结构，并且向Java程序员提供了访问方法区内的数据结构的接口


- 有两种类型的类加载器
  - java虚拟机自带的加载器
    - 根类加载器（Bootstrap）
    - 扩展类加载器（Extension） 
    - 系统（应用）类加载器（System）
  - 用户自定义的类加载器
    - java.lang.ClassLoader的子类
    - 用户可以定制类的加载方式
- 类加载器并不需要等到某个类被“首次主动使用”时再加载它
- JVM规范允许类加载器在预料某个类将要被使用时就预先加载它，如果在预先加载的过程中遇到了.class文件缺失或者存在错误，类加载器必须在程序首次主动使用该类时才报告错误（LinkageError错误）
- 如果这个类一直没有被程序主动使用，那么类加载器就不会报告这个错误

```shell
-XX:+TraceClassLoading
```

### 连接

类被加载后，就进入连接阶段。连接就是将已经读入到内存的类的二进制数据合并到虚拟机的运行时环境中去

####验证

- 类的验证的内容
  - 类文件的结构检查
  - 语义检查
  - 字节码验证
  - 二进制兼容性的验证

#### 准备

- 在准备阶段，Java虚拟机为类的静态变量分配内存，并设置默认的初始值

  ```java
  /**
   * 例如：对于以下Sample类，在准备阶段，将为int类型的静态变量a分配4个字节的内存空间，并且赋予默认值0
   */
  public class Sample {
      private static int a = 1;
      private static long b;
  
      static {
          b = 2;
      }
  }
  ```

#### 解析

### 初始化

- 在初始化阶段，Java虚拟机执行类的初始化语句，为类的静态变量赋予初始值。

- 在程序中，静态变量的初始化有两种途径
  - 在静态变量的声明出进行初始化

  - 在静态代码块中进行初始化

    ```java
    /**
     *例如：在以下代码中，静态变量a和b都被显示初始化，而静态变量c没有显示初始化，它将保持默认值0
     */
    public class Sample {
        private static int a = 1;    //在静态变量的声明出进行初始化
        private static long b;
        private static long c;
    
        static {
            b = 2;    //在静态代码块中进行初始化
        }
    }
    ```

- 静态变量的声明语句，以及静态代码块都被看做类的初始化语句，Java虚拟机会按照初始化语句在类文件中的先后顺序来依次执行它们。

  ```java
  /**
   * 例如：以下Sample类初始化后，它的静态变量a的取值为4
   */
  public class Sample {
      static int a = 1;    
      static { a = 2; } 
      static { a = 4; } 
      public static void main(String[] args) {
      	System.out.println("a=" + a);
      }
  }
  ```

- 类的初始化步骤
  - 假如这个类还没有被加载和连接，那就先进行加载和连接
  - 假如类存在直接父类，并且这个父类还没有被初始化，那就先初始化直接父类
  - 假如类中存在初始化语句，那就依次执行这些初始化语句

- 类的初始化时机
  - 主动使用（七种，重要）
    - 创建类的实例
    - 访问某个类或接口的静态变量，或者对该静态变量赋值
    - 调用类的静态方法
    - 反射（如Class.forName("com.test.Test")）
    - 初始化一个类的子类
    - Java虚拟机启动时被标明为启动类的类（java Test）
    - JDK1.7开始提供的动态语言支持：java.lang.invoke.MethodHandle实例的解析结果REF_getStatic，REF_putStatic，REF_invokeStatic句柄对应的类没有初始化，则初始化
    - 除了以上七种情况，其他使用Java类的方式都被看作是对类的被动使用，都不会导致类的初始化
  - 当java虚拟机初始化一个类时，要求它的所有父类都已经初始化，但是这条规则并不适用接口
    - 在初始化一个类时，并不会先初始化它所实现的接口
    - 在初始化一个接口时，并不会先初始化它的父接口
  - 因此，一个父接口并不会因为它的子接口或者实现类的初始化而初始化。只有当程序首次使用特定接口的静态变量时，才会导致该接口的初始化
  - 只有当程序访问的静态变量或静态方法确实在当前类或当前接口中定义时，才可以认为是对类或接口的主动使用
  - 调用ClassLoader类的loadClass方法加载一个类，并不是对类的主动使用，不会导致类的初始化

###类加载器

- 类加载器用来把类加载到Java虚拟机中。从JDK1.2版本开始，类的加载过程采用父亲委托机制，这种机制能更好地保证Java平台的安全。在此委托机制中，除了Java虚拟机自带的根类加载器以外，其余的类加载器都有且只有一个父加载器。当Java程序请求加载器loader1加载Sample类时，loader1首先委托自己的父加载器去加载Sample类，若父加载器能加载，则有父加载器完成加载任务，否则才由加载器loader1本身加载Sample类



- Java虚拟机自带了以下几种加载器
  - 根(Bootstrap)类加载器：该加载器没有父加载器。它负责加载虚拟机的核心类库，如java.lang.*等。例如java.lang.Object就是由根类加载器加载的。根类加载器从系统属性sun.boot.class.path所指定的目录中加载类库。根类加载器的实现依赖于底层操作系统，属于虚拟机的实现的一部分，它并没有继承java.lang.ClassLoader类
  - 扩展(Extension)类加载器：它的父加载器为根类加载器。它从java.ext.dirs系统属性所指定的目录中加载类库，或者从JDK安装目录的jre/lib/ext子目录（扩展目录）下加载类库，如果把用户创建的JAR文件放在这个目录下，也会自动由扩展类加载器加载。扩展类加载器是纯Java类，是java.lang.ClassLoader类的子类
  - 系统(System)类加载器：也称应用类加载器，它的父加载器为扩展类加载器。它从环境变量classpath或者系统属性java.class.path所指定的目录中加载类，它是用户自定义的类加载器的默认父加载器。系统类加载器是纯Java类，是java.lang.ClassLoader类的子类
  - 除了以上虚拟机自带的加载器外，用户还可以定制自己的类加载器。Java提供了抽象类java.lang.ClassLoader，所有用户自定义的类加载器都应该继承ClassLoader类

- 父亲委托机制

  ![](<https://www.developerfeed.com/public/wordpress/classloader.png>)



- 获取ClassLoader的途径

  ```java
  // 获得当前类的ClassLoader
  String.class.getClassLoader()
  // 获得系统的ClassLoader
  ClassLoader.getSystemClassLoader()
  // 获得当前线程上下文的ClassLoader
  Thread.currentThread().getContextClassLoader()
  // 获得调用者的ClassLoader
  DriverManager.getCallerClassLoader()
  ```

- Jar hell问题以及解决办法

  - 当一个类或者一个资源文件存在多个jar中，就会存在jar hell问题

  - 可以通过以下代码来诊断问题：

    ```java
    public static void main(String[] args) throws Exception{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String resouceName = "java/lang/String.class";
        Enumeration<URL> urls = classLoader.getResources(resouceName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url);
        }
    }
    ```

- 类加载器的父亲委托机制

  - 在父亲委托机制中，各个加载器按照父子关系形成了树形结构，除了根类加载器之外，其余的类加载器都有且只有一个父加载器

  ![](https://i.postimg.cc/vZftmwXM/1575911378x3661913030.png)

  ![](https://i.postimg.cc/4Nhz8RDt/1575911186x3661913030.png)


  - Bootstrap ClassLoader/启动类加载器 $JAVA_HOME中jre/lib/rt.jar里所有的class，由C++实现，不是ClassLoader子类
  - Extension ClassLoader/扩展类加载器 负责加载java平台中扩展功能的一些jar包，包括$JAVA_HOME中jre/lib/*.jar或-Djava.ext.dirs指定目录下的jar包
  - App ClassLoader/系统类加载器 负责加载classpath中指定的jar包及目录中class
  - 若有一个类加载器能够成功加载Test类，那么这个类加载器被称为定义类加载器，所有能成功返回Class对象引用的类加载器（包括定义类加载器）都被称为初始类加载器

    - 假设loader1实际加载了Sample类，则loader1为Sample类的定义类加载器，loader1与loader2为Sample类的初始类加载器

  - 需要指出的是，加载器之间的父子关系实际上指的是加载器对象之间的包装关系，而不是类之间的继承关系。一对父子加载器可能是同一个加载器的两个实例，也可能不是。在子加载器对象中包装了一个父加载器对象。

    ```java
    /**
     例如：以下loader1和loader2都是MyClassLoder类的实例，并且loader2包装了loader1，loader1是    loader2的父加载器
     */
    ClassLoader loader1 = new MyClassLoader();
    // 参数loader1将作为loader2的父加载器
    ClassLoader loader2 = new MyClassLoader(loader1);
    ```

  - 父亲委托机制的优点是能够提高软件系统的安全性。因此在此机制下，用户自定义的类加载器不可能加载应该由父加载器加载的可靠类，从而防止不可靠甚至恶意的代码代替由父加载器加载的可靠代码。例如java.lang.Object类总是由根类加载器加载，其他任何用户自定义的类加载器都不可能加载包含有恶意代码的java.lang.Object类

- 命名空间

  - 每个类加载器都有自己的命名空间，命名空间由该类加载器及所有父加载器所加载的类组成
  - 在同一个命名空间中，不会出现类的完整名字（包括类的包名）相同的两个类
  - 在不同的命名空间中，有可能会出现类的完整名字（包括类的包名）相同的两个类

- 创建用户自定义的类加载器

  - 要创建用户自定义的类加载器，只需要扩展java.lang.ClassLoader类，然后覆盖它的findClass(String name)方法即可，该方法根据参数指定类的名字，返回对应的Class对象的引用。

- 不同类加载器的命名空间的关系
  - 同一个命名空间内的类是互相可见的
  - 子加载器的命名空间包含所有父加载器的命名空间。因此由子加载器加载的类能看见父加载器加载的类。例如系统类加载器加载的类能看见根类加载器加载的类
  - 由父加载器加载的类不能看见子加载器加载的类
  - 如果两个加载器之间没有直接或间接的父子关系，那么它们各自加载的类相互不可见

### 类的卸载

- 当MySample类被加载、连接和初始化后，它的生命周期就开始了。当代表MySample类的Class对象不再被引用，即不可触及时，Class对象就会结束生命周期，MySample类在方法区的数据也会被卸载，从而结束MySample类的生命周期。

- 一个类何时结束生命周期，取决于代表它的Class对象何时结束生命周期

- 由Java虚拟机自带的类加载器所加载的类，在虚拟机的生命周期中，始终不会被卸载。前面已经介绍过，Java虚拟机自带的类加载器包括根类加载器、扩展类加载器和系统类加载器。Java虚拟机本身会始终引用这些类加载器，而这些类加载器则会始终引用它们所加载的类的Class对象，因此这些Class对象始终是可触及的。

- 由用户自定义的类加载器所加载的类是可以被卸载的


## 字节码

1. 使用javap -verbose命令分析一个字节码文件时，将会分析该字节码文件的魔数、版本号、常量池、类信息、类的构造方法、类中的方法信息、类变量与成员变量等信息。
2. 魔数：所有.class字节码文件的前4个字节都是魔数，魔数值为固定值：0xCAFEBABE。
3. 魔数之后的4个字节为版本信息，前两个字节表示minor version（此版本号），后两个字节表示major version（主版本号）。这里的版本为00 00 00 34，换算成十进制，表示此版本号为0，主版本号为52。所以该文件的版本号为：1.8.0。可以通过java -version命令来验证这一点。
4. 常量池（constant pool）：紧接着主版本号之后的就是常量池入口。一个Java类中定义的很多信息都是由常量池来维护和描述的，可以将常量池看做是Class文件的资源仓库，比如说Java类中定义的方法和变量信息，都是存储在常量池中。常量池中主要存储两类常量：字面量与符号引用。字面量如文本字符串，Java中声明为final的常量值等，而符号引用如类和接口的全局限定名，字段的名称和描述符等。
5. 常量池的总体结构：Java类所对应的常量池主要又常量池数量与常量池数组（常量表）这两部分共同构成。常量池数量紧跟在主版本号后面，占据2个字节；常量池数组则紧跟在常量池数量之后。常量池数组与一般的数组不同的是，常量池数组中不同的元素的类型、结构都是不同的，长度当然也就不同；但是每一种元素的第一个数据都是u1类型，该字节是个标志位，占据1个字节。JVM在解析常量池时，会根据这个u1类型来获取元素的具体类型。值得注意的是，常量池数组中元素的个数 = 常量池数 - 1（其中0暂时不使用），目的是满足某些常量池索引值的数据在特定情况下需要表达【不引用任何一个常量池】的含义；根本原因在于，索引为0也是一个常量（保留常量），只不过它不位于常量表中，这个常量就对应null值；所以，常量池的索引从1而非0开始。
6. 在JVM规范中，每个变量/字段都有描述信息，描述信息主要的作用是描述字段的数据类型、方法的参数列表（包括数量、类型与顺序）与返回值。根据描述符规则，基本数据类型和代表无返回值的void类型都用一个大写字符来表示，对象类型则使用字符L加对象的全限定名称来表示。为了压缩字节码文件的体积，对于基本数据类型，JVM都只使用一个大写字母来表示，如下所示：B - byte，C - char，D - double，F - float，I - int，J - long，S - short，Z - boolean，V - void，L - 对象类型，如Ljava/lang/String;
7. 对于数组类型来说，每一个维度使用的一个前置的[来表示，如int[]被记录为[I，String二维数组被记录为[[java/lang/String;
8. 用描述符描述方法是，按照先参数列表，后返回值的顺序来描述，参数列表按照参数的严格顺序放在一组()之内，如方法：String getRealnameByIdAndNickname(int id, String name)的描述符为：(I, Ljava/lang/String;) Ljava/lang/String;

```shell
# 字节码文件
javap MyTest
# 仅显示公共方法
javap -verbose MyTest
# -p 参数：显示私有方法
javap -verbose -p MyTest
```

### Java字节码整体结构

![](https://i.postimg.cc/nLdkyY0Q/1575989945x3703728804.png)



![bytecode](https://i.postimg.cc/xddG3Nnj/bytecode.png)

![](https://blog.lse.epita.fr/medias/images/Class_overview.png)

```c
ClassFile {
    u4             magic;
    u2             minor_version;
    u2             major_version;
    u2             constant_pool_count;
    cp_info        constant_pool[constant_pool_count-1];
    u2             access_flags;
    u2             this_class;
    u2             super_class;
    u2             interfaces_count;
    u2             interfaces[interfaces_count];
    u2             fields_count;
    field_info     fields[fields_count];
    u2             methods_count;
    method_info    methods[methods_count];
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```



### Java常量池结构

![](https://blog.lse.epita.fr/medias/images/Constant_pool.png)

###Java字节码结构

- Class字节码中有两种数据类型
  - 字节数据直接量：这是基本的数据类型。共细分为u1、u2、u4、u8四种，分别代表连续的1个字节、2个字节、4个字节、8个字节组成的整体数据。

  - 表（数组）：表是由多个基本数据或其他表，按照既定顺序组成的大的数据集合。表是有结构的，它的结构体体现在：组成表的成分所在的位置和顺序都是已经严格定义好的。


###Java字节码

![](https://i.postimg.cc/RVMTZDyn/1575990451x3661913030.png)

- 上面的表中描述了11种数据类型的结构，其实在jdk1.7之后又增加了3种（CONSTANT_MethodHandle_info，CONSTANT_MethodType_info以及CONSTANT_InvokeDynamic_info）。这样一共是14种

### Access_Flag访问标志

- 访问标志信息包括该Class文件是类还是接口，是否被定义成public，是否是abstract，如果是类，是否被声明成final。通过上面的源代码，我们知道该文件是类并且是public。

  ![](https://i.postimg.cc/26r17P4Q/1575990645x3661913030.png)

- 0x0021：是0x0020和0x0001的并集，表示ACC_PUBLIC与ACC_SUPER


![](https://blog.lse.epita.fr/medias/images/Access_flag.png)

### 类索引、父类索引与接口索引

- 根据上图可知

### 字段表集合

- 字段表用于描述类和接口中声明的变量。这里的字段包含了类级别变量以及实例变量，但是不包括方法内部声明的局部变量

- fields_count：u2

  ![](https://i.postimg.cc/6qNTpFQ2/1575990842x3703728804.png)

### 字段表结构

```c
field_info {
    u2             access_flags;
    u2             name_index;
    u2             descriptor_index;
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```

### 方法表

- methods_count：u2

  ![](https://i.postimg.cc/SscN3VGy/1575990948x3703728804.png)

### 方法表结构

- 前三个字段和field_info一样

```c
method_info {
    u2             access_flags;
    u2             name_index;
    u2             descriptor_index;
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```

### 方法的属性结构

- 方法中的每个属性都是一个attribute_info结构

```c
attribute_info {
    u2 attribute_name_index;
    u4 attribute_length;
    u1 info[attribute_length];
}
```

- JVM预定义了部分attribute，但是编译器自己也可以实现自己的attribute写入class文件里，供运行时使用。
- 不同的attribute通过attribute_name_index来区分

### JVM规范预定义的attribute

| Flag Name        | Value  | Interpretation                                               |
| ---------------- | ------ | ------------------------------------------------------------ |
| `ACC_PUBLIC`     | 0x0001 | Declared `public`; may be accessed from outside its package. |
| `ACC_FINAL`      | 0x0010 | Declared `final`; no subclasses allowed.                     |
| `ACC_SUPER`      | 0x0020 | Treat superclass methods specially when invoked by the *invokespecial* instruction. |
| `ACC_INTERFACE`  | 0x0200 | Is an interface, not a class.                                |
| `ACC_ABSTRACT`   | 0x0400 | Declared `abstract`; must not be instantiated.               |
| `ACC_SYNTHETIC`  | 0x1000 | Declared synthetic; not present in the source code.          |
| `ACC_ANNOTATION` | 0x2000 | Declared as an annotation type.                              |
| `ACC_ENUM`       | 0x4000 | Declared as an `enum` type.                                  |

### Code结构

- Code attribute的作用是保存该方法的结构，如所对应的字节码

```
Code_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 max_stack;
    u2 max_locals;
    u4 code_length;
    u1 code[code_length];
    u2 exception_table_length;
    {   u2 start_pc;
        u2 end_pc;
        u2 handler_pc;
        u2 catch_type;
    } exception_table[exception_table_length];
    u2 attributes_count;
    attribute_info attributes[attributes_count];
}
```

- attribute_length表示所包含的字节数，不包含attribute_name_index和attribute_length字段。
- max_stack表示这个方法运行的任何时刻所能达到的操作数栈的最大深度
- max_locals表示方法执行期间创建的局部变量的数目，包含用来表示传入的参数的局部变量
- code_length表示该方法所包含的字节码的字节数以及具体的指令码
- 具体字节码既是该方法被调用时，虚拟机所执行的字节码
- exception_table，这里存放的是处理异常的信息
- 每个exception_table表项由start_pc, end_pc, handler_pc, catch_type组成
- start_pc和end_pc表示在code数组中的从start_pc到end_pc处（包含start_pc, 不包含end_pc）的指令抛出的异常会由这个表项来处理
- handler_pc表示处理异常的代码的开始处。catch_type表示会被处理的异常类型，它指向常量池里的一个异常类。当catch_type为0时，表示处理所有的异常

### 附件属性

- 接下来是该方法的附加属性
- LineNumberTable：这个属性用来表示code数组中的字节码和Java代码运行行数之间的关系。这个属性可以用来在调试的时候定位代码执行的行数

### LineNumberTable的结构

```c
LineNumberTable_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 line_number_table_length;
    {   u2 start_pc;
        u2 line_number;	
    } line_number_table[line_number_table_length];
}
```

### 字节码查看工具

- <https://github.com/ingokegel/jclasslib>

### 内存空间

堆内存溢出

```shell
java -Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError -classpath jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.memory.MyTest1
```

虚拟机栈溢出

```shell
java -Xss160k -classpath jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.memory.MyTest2
```

死锁

```shell
java -classpath jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.memory.MyTest3
```

方法区 java.lang.OutOfMemoryError: Metaspace

```shell
java -XX:MaxMetaspaceSize=200m -XX:+TraceClassLoading -classpath jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.memory.MyTest4 
```

jcmd

```shell
jcmd 5911 help
5911:
The following commands are available:
VM.native_memory
ManagementAgent.stop
ManagementAgent.start_local
ManagementAgent.start
VM.classloader_stats
GC.rotate_log
Thread.print
GC.class_stats
GC.class_histogram
GC.heap_dump
GC.finalizer_info
GC.heap_info
GC.run_finalization
GC.run
VM.uptime
VM.dynlibs
VM.flags
VM.system_properties
VM.command_line
VM.version
help
```

```shell
jcmd 5911 VM.flags
5911:
-XX:CICompilerCount=2 -XX:InitialHeapSize=16777216 -XX:MaxHeapSize=260046848 -XX:MaxNewSize=86638592 -XX:MinHeapDeltaBytes=196608 -XX:NewSize=5570560 -XX:OldSize=11206656 -XX:ThreadStackSize=228 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops 
```

```shell
jcmd 5911 help VM.flags
```



##垃圾回收

###JVM运行时内存数据区域

![](https://i.postimg.cc/44bx1LdP/1575993195x3661913030.png)

- 程序计数器
- 本地方法栈
- Java虚拟机栈（JVM Stack）
  - Java虚拟机栈描述的是Java方法的执行模型；每个方法执行的时候都会创建一个帧（Frame）栈用于存放局部变量表，操作栈，动态链接，方法出口等信息。一个方法的执行过程，就是这个方法对于栈帧的入栈出栈过程
  - 线程隔离

- 堆（Heap）
  - 堆里存放的是对象的实例
  - 是Java虚拟机管理内存中最大的一块
  - GC主要的工作区域，为了高效的GC，会把堆细分更多的子区域
  - 线程共享

- 方法区域
  - 存放了每个Class的结构信息，包括常量池、字段描述、方法描述
  - GC的非主要工作区域

- 实例

  ```java
  public void method() {
      Object obj = new Object();
  }
  ```
  - 生成了2部分的内存区域，1）obj这个引用变量，因为是方法内的变量，放到JVM Stack里面 2）真正Object class的实例对象，放到Heap里面
  - 上述的new语句一共消耗12个bytes，JVM规定引用占4个bytes（在JVM Stack）,而空对象是8个bytes（在 Heap）

  - 方法结束后，对应Stack中的变量马上回收，但是Heap中的对象要等到GC来回收

### JVM垃圾回收（GC）模型

- 垃圾判断算法
- GC算法
- 垃圾回收器的实现和选择

####垃圾判断的算法

- 引用计数算法（Reference Counting）
- 根搜索算法（Root Tracing）

#####引用计数算法（Reference Counting）

- 给对象添加一个引用计数器，当有一个地方引用它，计数器加1，当引用失效，计数器减1，任何时刻计数器为0的对象就是不能再被使用的
- 引用计数算法无法解决对象循环引用的问题

#####根搜索算法（GC Root Tracing）

- 在实际的生产语言中（Java、C#等），都是使用根搜索算法判定对象是否存活
- 算法基本思路就是通过一系列的称为”GC Roots“的点作为起始进行向下搜索，当一个对象到GC Roots没有任何引用链（Reference Chain）相连，则证明此对象是不可用的
- 在Java语言中，GC Roots包括
  - 在VM栈（帧中的本地变量）中的引用
  - 方法区中的静态引用
  - JNI（即一般说的Native方法）中的引用

##### 方法区

- Java虚拟机规范表示可以不要求虚拟机在这区实现GC，这区GC的”性价比“一般比较低
- 在堆中，尤其是在新生代，常规应用进行一次GC一般可以回收70%~95%的空间，而方法区的GC效率远小于此
- 当前的商业JVM都有实现方法区的GC，主要回收两部分内容：废弃常量与无用类
- 主要回收两部分内容：废弃常量与无用类
- 类回收需要满足如下3个条件
  - 该类所有的实例都已经被GC，也就是JVM中不存在该Class的任何实例
  - 加载该类的ClassLoader已经被GC
  - 该类对应的java.lang.Class对象没有在任何地方被引用，如不能在任何地方通过反射访问该类的方法
- 在大量使用反射，动态代理、CGLib等字节码框架、动态生成JSP以及OSGI这类频繁自定义ClassLoader的场景都需要JVM具备类卸载的支持以保证方法区不会溢出

#### JVM常见GC算法

- 标记-清除算法（Mark-Sweep）
- 标记-整理算法（Mark-Compact）
- 复制算法（Copying）
- 分代算法（Generational）

##### 标记-清除算法（Mark-Sweep）

- 算法分为”标记“和”清除“两个阶段，首先标记出所有需要回收的对象，然后回收所有需要回收的对象

- 缺点

  - 效率问题，标记和清理两个过程效率都不高

  - 空间问题，标记清理之后会产生大量不连续的内存碎片，空间碎片太多可能会导致后续使用中无法找到足够的连续内存而提前触发另一次的垃圾搜集动作

    ![](https://i.postimg.cc/qR1ZJHwQ/1576078831x3661913030.png)

  ![](https://i.postimg.cc/k4YfrFMp/1576079364x3661913030.png)

  ​	![](https://i.postimg.cc/bw0gYjNb/1576079417x3661913030.png)

  ​	![](https://i.postimg.cc/Bn3yBpjT/1576079459x3661913030.png)

  ​	![](http://chuantu.xyz/t6/703/1576079486x3703728804.png)

  - 效率不高，需要扫描所有对象。堆越大，GC越慢
  - 存在内存碎片问题。GC次数越多，碎片越严重

##### 复制（Copying）收集算法

- 将可用内存划分为两块，每次只使用其中的一块，当半区内存用完了，仅将还存活的对象复制到另外一块上面，然后就把原来整块内存空间一次性清理掉

- 这样使得每次内存回收都是对整个半区的回收，内存分配时也就不用考虑内存碎片等复杂情况，只要移动堆顶指针，按顺序分配内存就可以了，实现简单，运行高效。只是这种算法的代价是将内存缩小为原来的一半，代价高昂

- 现在商业虚拟机中都是用了这一种收集算法来回收新生代

- 将内存分为一块较大的eden空间和2块较小的survivor空间，每次使用eden和其中一块survivor，当回收时将eden和survivor还存活的对象一次性拷贝到另外一块survivor空间上，然后清理掉eden和用过的survivor

- Oracle Hotspot虚拟机默认eden和survivor的大小比例是8:1，也就是每次只有10%的内存是”浪费的“

- 复制收集算法在对象存活率高的时候，效率有所下降

- 如果不想浪费50%的空间，就需要有额外的空间进行分配担保用于应付半区内存中所有对象都100%存活的极端情况，所以在老年代一般不能直接选用这种算法

  ![](https://i.postimg.cc/dQTxMpdR/1576080871x3661913030.png)

![](https://i.postimg.cc/jdbvp5RK/1576080900x3661913030.png)

![](https://i.postimg.cc/sg2PzGqF/1576080935x3661913030.png)

![](https://i.postimg.cc/y8WRt43s/1576080963x3661913030.png)

![](https://i.postimg.cc/W1DDSGx4/1576080990x3661913030.png)

- 只需要扫描存活对象，效率更高

- 不会产生碎片

- 需要浪费额外的内存作为复制区

- 复制算法非常适合生命周期比较短的对象，因为每次GC总能回收大部分对象，复制的开销比较小

- 根据IBM的专家研究，98%的Java对象只会存活I个GC周期，对这些对象很适合用复制算法。而且1:1

  的划分工作区和复制区的空间

##### 标记-整理算法（Mark-Compact）

- 标记过程仍然一样，但后续步骤不是进行直接清理，而是令所有存活的对象一端移动，然后直接清理掉这端边界以外的内存。

  ![](https://i.postimg.cc/kXLMJkhn/1576081537x3703728804.png)

- 没有内存碎片

- 比Mark-Sweep耗费更多的时间进行compact

##### 分代算法（Generational GC）

- 当前商业虚拟机的垃圾收集都是采用”分代收集“（Generational Collecting）算法，根据对象不同的存活周期将内存划分为几块。
- 一般是把Java堆分为新生代和老年代，这样就可以根据各个年代的特点采用最适当的收集算法，譬如新生代每次GC都有大批对象死去，只有少量存活，那就选用复制算法只需要付出少量存活对象的复制成本就可以完成收集。
- 综合前面几种GC算法的优缺点，针对不同生命周期的对象采用不同的GC算法

![](https://i.postimg.cc/bwXTxwF4/1576082245x3703728804.png)

- Hotspot JVM 6中共划分为三个代：年轻代（Young Generation）、老年代（Old Generation）和永久代（Permanent Generation）。

![](https://i.postimg.cc/k5mTmgQW/1576082509x3661913030.png)

- 年轻代（Young Generation）
  - 新生代的对象都放在新生代。年轻代用复制算法进行GC（理论上，年轻代对象的生命周期非常短，所以适合复制算法）
  - 年轻代分为三个区。一个Eden区，两个Survivor区（可以通过参数设置Survivor个数）。对象在Eden区中生成。当Eden区满时，还存活的对象将被复制到一个Survivor区，当这个Survivor区满时，此区存活的对象将被复制到另外一个Survivor区，当第二个Survivor区也满的时候，从第一个Survivor区复制过来的并且此时还存活的对象，将被复制到老年代。2个Survivor是完全对称，轮流替换。
  - Eden和2个Survivor的缺省比例是8:1:1，也就是10%的空间会被浪费。可以根据GC log信息调整大小的比例
- 老年代（Old Generation）
  - 存放了经过一次或多次GC还存活的对象
  - 一般采用Mark-Sweep或者Mark-Compact算法进行GC
  - 有多种垃圾收集器可以选择。每种垃圾收集器可以看做一个GC算法的具体实现。可以根据具体应用的需求选用适合的垃圾收集器（追求吞吐量？追求最短的响应时间？）

- ~~永久代~~
  - 并不属于堆（Heap）.但是GC也会涉及到这个区域
  - 存放了每个Class的结构信息，包括常量池、字段描述、方法描述。与垃圾收集要收集的Java对象关系不大

#### 内存结构

![](https://i.postimg.cc/k5KLQHcv/1576083924x3661913030.png)



备注：在Hotspot中本地方法栈和JVM方法栈是同一个，因此也可用-Xss控制

#### 内存分配

1. 堆上分配

   大多数情况在eden上分配，偶尔会直接在old上分配

   细节取决于GC实现

2. 栈上分配

   原子类型的局部变量

#### 内存回收

- GC要做的是将那些dead的对象所占用的内存回收掉
  - Hotspot认为没有引用的对象是dead的
  - Hotspot将引用分为四种：Strong、Soft、Weak、Phantom
    - Strong即默认通过Object o=new Object()这种方法赋值的引用
    - Soft、Weak、Phantom这三种则都是继承Reference
- 在Full GC时会对Reference类型的应用进行特殊处理
  - Soft：内存不够时一定会被GC、长期不用也会被GC
  - Weak：一定会被GC，当被mark为dead，会在ReferenceQueue中通知
  - Phantom：本来就没有引用，当从jvm heap中释放时会通知

#### 垃圾收集算法

![](https://i.postimg.cc/KjGXv5tr/1576084881x3661913030.png)

#### GC的时机

- 在分代模型的基础上，GC从时机上分为两种：Scavenge GC和Full GC
- Scavenge GC（Minor GC）
  - 触发时机：新对象生成时，Eden空间满了
  - 理论上Eden区大多数对象会在Scavenge GC回收，复制算法的执行效率会很高，Scavenge GC时间比较短
- Full GC
  - 对整个JVM进行整理，包括Young、Old和Perm
  - 主要的触发时机：1）Old满了 2）Perm满了 3）System.gc()
  - 效率很低，尽量减少Full GC。

#### 垃圾回收器（Garbage Collector）

- 分代模型：GC的宏观愿景；
- 垃圾回收器：GC的具体实现
- Hotspot JVM提供多种垃圾回收器，我们需要根据具体应用的需要采用不同的回收器
- 没有万能的垃圾回收器，每种垃圾回收器都有自己的适用场景

#### 垃圾收集器的”并行“和”并发“

- 并行（Parallel）：指多个收集器的线程同时工作，但是用户线程处于等待状态
- 并发（Concurrent）：指收集器在工作的同时，可以允许用户线程工作。
  - 并发不代表解决了GC停顿问题，在关键的步骤还是要停顿的。比如在收集器标记垃圾的时候。但在清除垃圾的时候，用户线程可以和GC线程并发执行。

#### Serial收集器

- 当线程收集器，收集时会暂停所有工作线程（Stop The World，简称STW），使用复制收集算法，虚拟机运行在Client模式时的默认新生代收集器。
- 最早的收集器，单线程进行GC
- New和Old Generation都可以使用
- 在新生代，采用复制算法；在老年代，采用Mark Compact算法
- 因为是单线程GC，没有多线程切换的额外开销，简单实用
- Hotspot Client模式下缺省的收集器

![](https://i.postimg.cc/4yPgz0XS/1576086337x3661913030.png)

#### ParNew收集器

- ParNew收集器就是Serial的多线程版本，除了使用多个收集线程外，其余行为包括算法、STW、对象分配规则、回收策略等都与Serial收集器一模一样
- 对应的这种收集器是虚拟机运行在Server模式的默认新生代收集器，在单个CPU的环境中ParNew收集器并不会比Serial收集器有更好的效果
- Serial收集器在新生代的多线程版本
- 使用复制算法（因为针对新生代）
- 只有在多CPU环境下，效率才会比Serial收集器高
- 可以通过-XX:ParallelGCThreads来控制GC线程数的多少。需要结合具体CPU的个数
- Server模式下新生代的缺省收集器

#### Parallel Scavenge收集器

- Parallel Scavenge收集器也是一个多线程收集器，也是使用复制算法，但它的对象分配规则与回收策略都与ParNew收集器有所不同，它是以吞吐量最大化（即GC时间占总运行时间最小）为目标的收集器实现，它运行较长时间的STW换取总吞吐量最大化

#### Serial Old收集器

- Serial Old是单线程收集器，使用标记-整理算法，是老年代的收集器

#### Parallel Old收集器

- 老年代版本吞吐量优先收集器，使用多线程和标记-整理算法，JVM 1.6提供，在此之前，新生代使用PS收集器的话，老年代除了Serial Old外别无选择，因为PS无法与CMS收集器配合工作
- Parallel Scavenge在老年代的实现
- 在JVM 1.6才出现Parallel Old
- 采用多线程，Mark-Compact算法
- 更注重吞吐量
- Parallel Scavenge + Parallel Old = 高吞吐量，但GC停顿可能不太理想

![](https://i.postimg.cc/sD3fvY3L/1576087954x3661913030.png)

#### CMS（Concurrent Mark Sweep）收集器

- CMS是一种以最短停顿时间为目标的收集器，使用CMS并不能达到GC效率最高（总体GC时间最小），但它能尽可能降低GC时服务的停顿时间，CMS收集器使用的是标记-清除算法
- 追求最短停顿时间，非常适合Web应用
- 只针对老年区，一般结合ParNew使用
- Concurrent，GC线程和用户线程并发工作（尽量并发）
- Mark-Sweep
- 只有在多CPU环境下才有意义
- 使用-XX:+UseConcMarkSweepGC打开

##### CMS收集器的缺点

- CMS以牺牲CPU资源的代价来减少用户线程的停顿。当CPU个数少于4的时候，有可能对吞吐量影响非常大
- CMS在并发清理的过程中，用户线程还在跑。这时候需要预留一部分空间给用户线程
- CMS用Mark-Sweep，会带来碎片问题。碎片过多的时候会容易频繁触发Full GC

#### GC垃圾收集器的JVM参数定义

![](https://i.postimg.cc/t4Cgmhf6/1576163929x3703728804.png)

![](http://chuantu.xyz/t6/703/1576164072x3703728804.png)

#### Java内存泄漏的经典原因

- 对象定义在错误的范围（Wrong Scope）
- 异常（Exception）处理不当
- 集合数据管理不当

##### 对象定义在错误的范围（Wrong Scope）

- 如果Foo实例对象的生命较长，会导致临时性内存泄漏。（这里的names变量其实只有临时作用）

  ![](https://i.postimg.cc/CLCZ2Mx3/1576164597x3661913030.png)

- JVM喜欢生命周期短的对象，这样做已经足够高效

  ![](https://i.postimg.cc/kgcb2MrQ/1576164673x3703728804.png)

##### 异常处理不当

- 错误的做法

  ![](https://i.postimg.cc/50G9XTNq/1576164771x3661913030.png)

- 正确的做法

  ![](https://i.postimg.cc/0ypzLk2g/1576164918x3661913030.png)

##### 数据集合管理不当

- 当使用Array-based的数据结构（ArrayList，HashMap等）时，尽量减少resize
  - 比如new ArrayList时，尽量估算size，在创建的时候把size确定
  - 减少resize可以避免没有必要的array copying，gc碎片等问题
- 如果一个List只需要顺序访问，不需要随机访问（Random Access），用LinkedList代替ArrayList
  - LinkedList本质是链表，不需要resize，但只适用于顺序访问

```shell
java -verbose:gc -XX:+PrintGCDetails -Xms20m -Xmx20m -Xmn10m -XX:SurvivorRatio=8 -classpath jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.gc.MyTest1
```

```shell
# -XX:PretenureSizeThreshold要与-XX:+UseSerialGC配合使用
java -verbose:gc -XX:+PrintGCDetails -Xms20m -Xmx20m -Xmn10m -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=4194304 -XX:+UseSerialGC -classpath jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.gc.MyTest2
```

```shell
java -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=5 -XX:+PrintTenuringDistribution jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.gc.MyTest3
```

```shell
java -Xmx200m -Xmn50m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:TargetSurvivorRatio=60 -XX:+PrintTenuringDistribution -XX:MaxTenuringThreshold=3 -verbose:gc -XX:+PrintGCDetails
-XX:+PrintGCDateStamps  -classpath jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.gc.MyTest4
```



### CMS

Concurrent Mark Sweep

#### 枚举根节点

- 当执行系统停顿下来后，并不需要一个不漏地检查完所有执行上下文和全局的引用位置，虚拟机应当是有办法直接得知哪些地方存放着对象应用。在HotSpot的实现中，是使用一组称为OopMap的数据结构来达到这个目的的。

#### 安全点

- 在OopMap的协助下，HotSpot可以快速且准确地完成GC Roots枚举，但一个很现实的问题随之而来：可能导致引用关系变化，或者说OopMap内容变化的指令非常多，如果为每一条指令都生成对应的OopMap，那将会需要大量的额外空间，这样GC的空间成本将会变得更高
- 实际上，HotSpot并没有为每条指令都生成OopMap，而只是在“特殊的位置”记录了这些信息，这些位置称为安全的（Safepoint），即程序执行时并非在所有地方都能停顿下来开始GC，只有在到达安全点时才能暂停。
- Safepoint的选定既不能太少以至于让GC等待时间太长，也不能过于频繁以至于过分增大运行时的负载。所以，安全点的选定基本上是以“是否具有让程序长时间执行的特征”为标准进行选定的—因为每条指令执行的时间非常短暂，程序不太可能因为指令流长度太长这个原因而过长时间运行，“长时间执行”的最明显特征就是指令序列复用，例如方法调用、循环跳转、异常跳转等，所以具有这些功能的指令才会产生Safepoint。
- 对于Safepoint，另一个需要考虑的问题是如何在GC发生时让所有线程（这里不包括执行JNI调用的线程）都“跑”到最近的安全点上再停顿下来：抢占式中断（Preemptive Suspension）和主动式中断（Voluntary Suspension）
- 抢占式中断：它不需要线程的执行代码主动去配合，在GC发生时，首先把所有线程全部中断，如果有线程中断的地方不再安全点上，就恢复线程，让它“跑”到安全点上。
- 主动式中断：当GC需要中断线程的时候，不直接对线程操作，仅仅简单地设置一个标志，各个线程执行时主动去轮询这个标志，发现中断标志为真时就自己中断挂起。轮询标志的地方和安全点是重合的，另外再加上创建对象需要分配内存的地方。

现在几乎没有虚拟机采用抢占式中断来暂停线程从而响应GC事件

#### 安全区域

- 在使用Safepoint似乎已经完美的解决了如何进入GC的问题，但实际上情况却并不一样。Safepoint机制保证了程序执行时，在不太长的时间内就会遇到可进入GC的Safepoint。但如果程序在“不执行”的时候呢？所谓程序不执行就是没有分配CPU时间，典型的列子就是处于Sleep状态或者Blocked状态，这时候线程无法响应JVM的中断请求，JVM也显然不太可能等待线程重新分配CPU时间。对于这种情况，就需要安全区域（Safe Regin）来解决了
- 在线程执行到Safe Regin中的代码时，首先标识自己已经进入了Safe Regin，那样，当在这段时间里JVM要发起GC时，就不用管标识自己为Safe Regin状态的线程了。在线程要离开Safe Regin时，它要检查系统是否已经完成了根节点枚举（或者是整个GC过程），如果完成了，那线程就继续执行，否则它就必须等待直到收到可以安全离开Safe Regin的信号为止。 

#### CMS 收集器

- CMS（Concurrent Mark Sweep）收集器，以获取最短回收停顿时间为目标，多数应用于互联网站或者B/S系统的服务端上。

- CMS是基于“标记-清除”算法实现的，整个过程分为4个步骤：

  - 初始标记（CMS initial mark）
  - 并发标记（CMS concurrent mark）
  - 重新标记（CMS remark）
  - 并发清除（CMS concurrent sweep）

- 其中，初始标记、重新标记这两个步骤仍然需要“Stop The World”；

- 初始标记只是标记一下GC Roots能直接关联到的对象，速度很快；

- 并发标记阶段就是进行GC Roots Tracing的过程；

- 重新标记阶段则是为了修正并发标记期间因用户称需继续运作而导致标记产生变动的那一部分对象标记记录，这个阶段的停顿时间一般会比初始标记阶段稍长一些，但远比并发标记的时间短。

- CMS收集器的运作步骤如下图所示，在整个过程中耗时最长的并发标记和并发清除过程收集器线程都可以与用户线程一起工作，因此，从总体上看，CMS收集器的内存回收过程是与用户线程一起并发执行的。

  ![](https://i.postimg.cc/gJMXNgcn/1576337170x3661913030.png)

- 优点

  - 并发收集、低停顿，Oracle公司的一些官方文档中也称之为并发低停顿收集器（Concurrent Low Pause Collector）

- 缺点

  - CMS收集器对CPU资源非常敏感。
  - CMS收集器无法处理浮动垃圾（Floating Garbage），可能出现“Concurrent Mode Failure”失败而导致另一次Full GC的产生。如果在应用中老年代增长不是太快，可以适当调高参数-XX:CMSInitiatingOccupancyFraction的值来提高触发百分比，以便降低内存回收次数从而获取更好地性能。要是CMS运行期间预留的内存无法满足程序的需要时，虚拟机将启动后背预案：临时启用Serial Old收集器来重新进行老年代的垃圾收集，这样停顿时间就很长了。所以说参数-XX:CMSInitiatingOccupancyFraction设置太高很容易导致大量“Concurrent Mode Failure”失败，性能反而降低。
  - 收集结束时会有大量空间碎片产生，空间碎片过多时，将会给大对象分配带来很大麻烦，往往出现老年代还有很大空间剩余，但是无法找到足够大的连续空间来分配当前对象，不得不提前进行一次Full GC。CMS收集器提供了一个-XX:UseCMSCompactAtFullCollection开关参数（默认就是开启的），用于在CMS收集器顶不住要进行Full GC时开启内存碎片的合并整理过程，内存整理的过程是无法并发的，空间碎片问题就没了，但停顿时间不得不变长。

#### 空间分配担保

- 在发生Minor GC之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间，如果这个条件成立，那么Minor GC可以确保是安全的。当大量对象在Minor GC后仍然存活，就需要老年代进行空间分配担保，把Survivor无法容纳的对象直接进入老年代。如果老年代判断到剩余空间不足（根据以往每一次回收晋升到老年代对象容量的平均值作为经验值），则进行一次Full GC。

#### CMS收集器收集步骤

- Phase 1：Initial Mark
- Phase 2：Concurrent Mark
- Phase 3：Concurrent Preclean
- Phase 4：Concurrent Abortable Preclean
- Phase 5：Final Mark
- Phase 6：Concurrent Sweep
- Phase 7：Concurrent Reset

##### Phase 1：Initial Mark

- 这个是CMS两次stop-the-world收集的其中一次，这个阶段的目标是：标记哪些直接被GC root引用或者被年轻代存活对象所引用的所有对象

  ![](https://i.postimg.cc/Xvq0GVGt/2019-12-15-12-20-09.png)

##### Phase 2：Concurrent Mark

- 在这个阶段Garbage Collector会遍历老年代，然后标记所有存活的对象，它会根据上个阶段找到的GC Roots遍历查找。并发标记阶段，它会与用户的应用程序并发运行。并不是老年代所有的存活对象都会被标记，因为在标记期间用户的程序可能会改变一些引用

![](https://i.postimg.cc/V64M2Ygs/1576340880x3703728804.png)

- 在上面的图中，与阶段1的图进行对比，就会发现有一个对象的引用已经发生了变化

##### Phase 3：Concurrent Preclean

- 这也是一个并发阶段，与应用的线程并发运行，并不会stop应用的线程。这并发运行的过程中，一些对象的引用可能会发生变化，但是这种情况发生时，JVM会将包含这个对象的区域（Card）标记为Dirty，这也就是Card Marking

- 在pre-clean阶段，哪些能够从Dirty对象到达的对象也会被标记，这个标记做完之后，dirty card标记就会被清除了

  ![](https://i.postimg.cc/prGf525c/1576341310x3703728804.png)

![](https://i.postimg.cc/8zFHWrHR/1576341338x3661913030.png)

##### Phase 4：Concurrent Abortable Preclean

- 这是一个并发阶段，但是同样不会影响用户的应用线程，这个阶段是为了尽量承担STW（stop-the-world）中最终标记记得的工作。这个阶段持续时间依赖于很多因素，由于这个阶段是在重复做很多相同的工作，直接满足一些条件（比如：重复迭代的次数、完成的工作量或者时钟时间等）

##### Phase 5：Final Mark

- 这是第二个STW阶段，也是CMS中的最后一个，这个阶段的目标是标记老年代所有的存活对象，由于之前的阶段是并发执行的，gc线程可能跟不上应用程序的变化，为了完成标记老年代所有存活对象的目标，STW就非常有必要了。
- 通常CMS的Final Mark阶段会在年轻代尽可能干净的时候运行，目的是为了减少连续STW发生的可能性（年轻代存活对象过多的话，也会导致老年代涉及的存活对象会很多）。这个阶段会比前面的几个阶段更复杂一些

##### 标记阶段完成

- 经历过这五个阶段之后，老年代所有存活的对象是被标记过了，现在可以通过清除算法去清理哪些老年代不再使用的对象

##### Phase 6：Concurrent Sweep

- 这里不需要STW，它是与用户的应用程序并发运行，这个阶段是：清除哪些不再使用的对象，回收它们的占用空间为将来使用

  ![](https://i.postimg.cc/BZcwQDYt/1576342244x3703728804.png)

##### Phase 7：Concurrent Reset

- 这个阶段也是并发执行的，它会重设CMS内部的数据结构，为下次的GC做准备

#### 总结

- CMS通过将大量工作分散到并发处理阶段来减少STW时间，在这块做得非常优秀，但是CMS也有一些其他的问题
- CMS收集器无法处理浮动垃圾（Floating Garbage），可能出现“Concurrent Mode Failure”失败而导致另一次Full GC的产生，可能引发串行Full GC
- 空间碎片，导致无法分配大对象，CMS收集器提供一个-XX:+UseCMSCompactAtFullCollection开关参数（默认就是开启的），用于在CMS收集器顶不住要进行Full GC时开启内存碎片的合并整理过程，内存整理的过程是无法并发的，空间碎片问题没有了，但停顿时间不得不变长
- 对于堆比较大的应用，GC的事件难以预估

```shell
java -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -verbose:gc -XX:+PrintGCDetails  -classpath jvm_lecture-1.0-SNAPSHOT.jar com.shengsiyuan.jvm.gc.MyTest5
```

### G1

Carbage First Collector

#### 吞吐量

- 吞吐量关注的是，在一个指定的时间内，最大化一个应用的工作量。
- 如下方式来衡量一个系统吞吐量的好坏：
  - 在一个小时内同一个事务（或者任务、请求）完成的次数（tps）
  - 数据库一小时可以完成多少次查询
- 对于关注吞吐量的系统，卡顿是可以接受的，因为这个系统关注长时间的大量任务的执行能力，单次快速的响应并不值得考虑。

#### 响应能力

- 响应能力是指一个程序或者系统对请求是否能够及时响应，比如
  - 一个桌面UI能多快地响应一个事件
  - 一个网站能够多快返回一个页面请求
  - 数据库能够多快返回查询的数据
- 对于这类对响应能力敏感的场景，长时间的停顿时无法接受的。

#### G1 Carbage Collector

- g1收集器是一个面向服务端的垃圾收集器，适用于多核处理器、大内存容量的服务端系统。
- 它满足短时间gc停顿的同时达到一个较高的吞吐量。
- JDK 7以上版本适用

##### G1收集器的设计目标

- 与应用线程同时工作，几乎不需要stop the world（与CMS类似）

- 整理剩余空间，不产生内存碎片（CMS只能在Full GC时，用stop the world整理内存碎片）
- GC停顿更加可控
- 不牺牲系统的吞吐量
- gc不要求额外的内存空间（CMS需要预留空间存储浮动垃圾）

##### G1的设计规划是要替换掉CMS

- G1在某些方面弥补了CMS的不足，比如CMS使用的是mark-sweep算法，自然会产生内存碎片；然而G1基于copying算法，高效的整理剩余内存，而不需要管理内存碎片。
- 另外，G1提供了更多的手段，已达到对gc停顿时间的可控。

##### Hotspot虚拟机主要构成

![](https://i.postimg.cc/NF7n32V9/1576393097x3703728804.png)

##### 传统垃圾收集器堆结构

![](https://i.postimg.cc/jjmBPbP5/1576393243x3703728804.png)

##### G1收集器堆结构

![](https://i.postimg.cc/nhkH4ChL/1576393360x3661913030.png)

- heap被划分为一个个相等的不连续的内存区域（regions），每个regions都有一个分代的角色：eden、survivor、old
- 对每个角色的数量并没有强制的限定，也就是说对每种分代内存的大小，可以动态变化
- G1最大的特点就是高效的执行回收，优先去执行那些大量对象可回收的区域（region）
- G1使用了gc停顿可预测的模型，来满足用户设定的gc停顿时间，G1会自动地选择哪些regions要清除，一次清除多少个region
- G1从多个region中复制存活的对象，然后集中放入一个region中，同时整理、清除内存（copying收集算法）

##### G1 vs CMS

- 对比使用mark-sweep的CMS，G1使用的copying算法不会造成内存碎片；
- 对于Parallel Scavenge（基于copying）、Parallel Old收集器（基于mark-compact-sweep），Parallel会对整个区域做整理导致gc停顿会比较长，而G1只是特定地整理几个region
- G1并非一个实时的收集器，与Parallel Scavenge一样，对gc停顿时间的设置并不绝对生效，只是G1有较高的几率保证不超过设定的gc停顿时间。与之前的gc收集器对比，G1会根据用户设定的gc停顿时间，智能评估那几个region需要被回收可以满足用户的设定

##### G1重要概念

- 分区（Region）：G1采取了不同的策略来解决并行、串行和CMS收集器的碎片、暂停时间不可控等问题-G1将整个堆分成相同大小的分区（Region）

  ![](https://i.postimg.cc/nhkH4ChL/1576393360x3661913030.png)

- 每个分区都可能是年轻代也可能是老年代，但是在同一时刻只能属于某个代。年轻代、幸存区、老年代这些概念还存在，成为逻辑上的概念，这样方便复用之前分代框架 的逻辑。

- 在物理上不需要连续，则带来了额外的好处-有的分区内垃圾对象特别多，有的分区内垃圾对象很少，G1会优先回收垃圾对象特别多的分区，这样可以花费较少的时间来回收这些分区的垃圾，这也就是G1名字的由来，即首先收集垃圾最多的分区。

- 依然是在新生代满了的时候，对整个新生代进行回收---整个新生代的对象，要么被回收、要么晋升，至于新生代也采取分区机制的原因，则是因为这样跟老年代的策略统一，方便调整代的大小

- G1还是一种带压缩的收集器，在回收老年代的分区时，是将存活的对象从一个分区拷贝到另外一个可用分区，这个拷贝过程实现了局部的压缩。

- 收集集合（CSet）：一组可被回收的分区的集合。在CSet中存活的数据会在GC过程中被移动到另外一个可用分区，CSet中的分区可以来自eden空间、survivor空间，或者老年代

- 已记忆集合（RSet）：RSet记录了其他Region中的对象引用本Region中对象的关系，属于points-info结构（谁引用了我对象）。RSet的价值在于使得垃圾收集器不需要扫描整个堆找到谁引用了当前分区中的对象，只需要扫描RSet即可。

- Region1和Region3中的对象都引用了Region2中的对象，因此在Region2的RSet中记录了这两个引用。

  ![](https://i.postimg.cc/50tWq3k7/1576396247x3703728804.png)

- G1 GC是在points-out的card table之上再加了一层结构来构成points-info RSet：每个region会记录下到底哪些别的region有指向自己的指针，而这些指针分别在哪些card的范围内。
- 这个RSet其实是一个hash table，key是别的region的起始地址，value是一个集合，里面的元素是card table的index。举例来说，如果region A的RSet里有一项的key是region B，value里面的index为1234的card，它的意思就是region B的一个card里有引用指向region A。所以对region A来说，该RSet记录的是points-info的关系；而card table仍然记录了points-out的关系。
- Snapshot-At-The-Beginning（SATB）：SATB是G1 GC在并发标记阶段使用的增量式标记算法。
- 并发标记是并发多线程的，但并发线程在同一个时刻只扫描一个分区

##### G1相对于CMS的优势

- G1在压缩空间方面有优势
- G1通过将内存空间分配成区域（Region）的方式避免内存碎片问题
- Eden、Survivor、Old区不再固定，在内存使用效率上来说更灵活
- G1可以通过设置预期停顿时间（Pause Time）来控制垃圾收集时间，避免应用雪崩现象
- G1在回收内存后会马上同时做合并空闲内存的工作，而CMS默认是在STW（stop the world）的时候做
- G1会在Young GC中使用，而CMS只能在Old区使用

##### G1的适合场景

- 服务端多核CPU、JVM内存占用较大的应用
- 应用在运行过程中会产生大量内存碎片、需要经常压缩空间
- 想要更可控、可预期的GC停顿周期；防止高并发下应用的雪崩现象

##### G1 GC模式

- G1提供了两种GC模式，Young GC和Mixed GC，两种都是完全Stop The World的
- Young GC：选定所有年轻代里的Region。通过控制年轻代的Region个数，即年轻代内存大小，来控制Young GC的时间开销。
- Mixed GC：选定所有年轻代里的Region，外加根据global concurrent marking统计得出收集收益高的若干老年代Region。在用户指定的开销目标范围内尽可能选择收益高的老年代Region
- Mixed GC不是Full GC，它只能回收部分老年代的Region，如果Mixed GC实在无法跟上程序分配内存的速度，导致老年代填满无法继续进行Mixed GC，就会使用serial old GC（Full GC）来收集整个GC heap。所以本质上，G1是不提供Full GC的

##### global concurrent marking

- global concurrent marking的执行过程类似于CMS，但是不同的是，在G1 GC中，它主要是为Mixed GC提供标记服务的，并不是一次GC过程的一个必须环节。
- global concurrent marking的执行过程分为四个步骤
  - 初始标记（initial mark，STW）：它标记了从GC Root开始直接可达的对象。
  - 并发标记（Concurrent Marking）：这个阶段从GC Root开始对heap中的对象进行标记，标记线程与应用线程并发执行，并且收集各个Region的存活对象信息
  - 重新标记（Remark，STW）：标记哪些在并发标记阶段发生变化的对象，将被回收
  - 清理（Cleanup）：清除空Region（没有存活对象的），加入到free list
- 第一阶段initial mark是共用了Young GC的暂停，这是因为它们可以复用root scan操作，所以可以说global concurrent marking是伴随Young GC而发生的。
- 第四阶段Cleanup只是回收了没有存活对象的Region，所以它并不需要STW

##### G1在运行过程中的主要模式

- YGC（不同于CMS）

- 并发阶段
- 混合模式
- Full GC（一般是G1出现问题时发生）
- G1 YCG在Eden充满时触发，在回收之后所有之前属于Eden的区块全部变成空白，即不属于任何一个分区（Eden、Survivor、Old）

##### Mixed GC

- 什么时候发生Mixed GC？
- 由一些参数控制，另外也控制着那些老年代Region会被选入CSet（收集集合）
- G1HeapWastePercent：global concurrent marking结束之后，我们可以知道old gen regions中有多少空间要被回收，在每次YGC之后和再此发生Mixed GC之前，会检查垃圾占比是否达到次参数，只有达到了，下次才会发生Mixed GC
- G1MixedGCLiveThresholdPercent： old generation region中的存活对象的占比，只有在此参数之下，才会被选入CSet
- G1MixedGCCountTarget：一次global concurrent marking之后，最多执行Mixed GC的次数
- G1OldCSetRegionThresholdPercent：一次Mixed GC中能被选入CSet的最多old generation region数量

![](https://i.postimg.cc/RCpj9v6Y/2019-12-15-11-39-10.png)

##### G1收集器概览

- G1算法将堆划分为若干个区域（Region），它仍然属于分代收集器。不过，这些区域的一部分包含新生代，新生代的垃圾收集依然采用暂停所有应用线程的方式，将存活对象拷贝到老年代或Survivor空间。老年代也分成很多区域，G1收集器通过将对象从一个区域复制到另外一个区域，完成清理工作。这就意味着，在正常的处理过程中，G1完成了堆的压缩（至少是部分堆的压缩），这样也就不会有CMS内存碎片问题的存在了

##### Humongous区域

- 在G1中，还有一种特殊的区域，叫Humongous区域。如果一个对象占用的空间达到或是超过了分区容量的50%以上，G1收集器就会认为这是一个巨型对象。这些巨型对象，默认直接会被分配在老年代，但是如果它是一个短期存在的巨型对象，就会对垃圾收集器造成负面影响。为了解决这个问题，G1划分为一个Humongous区，它用来专门存放巨型对象。如果一个H区装不下一个巨型对象，那么G1会寻找连续的H分区来存储。为了能找到连续的H区，有时候不得不启动Full GC

##### G1 Young GC

- Young GC主要是对Eden区进行GC，它在Eden空间耗尽时会被触发。在这种情况下，Eden空间的数据移动到Survivor空间中，如果Survivor空间不够，Eden空间的部分数据会直接晋升到老年代空间。Survivor区的数据移动到新的Survivor区中，也有部分数据晋升到老年代空间中。最终Eden空间的数据为空，GC完成工作，应用线程继续执行

- 如果仅仅GC新生代对象，我们如何找到所有的根对象呢？老年代的所有对象都是根么？那这样扫描下来会耗尽大量的时间。于是，G1引进了RSet的概念。它的全称是Remembered Set，作用是跟踪指向某个heap区中的对象引用

  ![](https://i.postimg.cc/VLJZsc8G/2019-12-17-8-34-02.png)

- CMS中，也有RSet的概念，在老年代中有一块区域用来记录指向新生代的引用。这是一种point-out，在进行Young GC时，扫描根时，仅仅需要扫描这一块区域，而不需要扫描整个老年代
- 但在G1中，并没有使用point-out，这是由于一个分区太小，分区数量太多，如果是用point-out的话，会造成大量的扫描浪费，有些根本不需要GC的分区引用也扫描了
- 于是G1中使用point-in来解决。point-in的意思是哪些分区引用了当前分区的对象。这样，仅仅将这些对象当做根来扫描就避免了无效的扫描。
- 由于新生代有多个，那么我们需要在新生代之间记录引用吗？这是不必要的，原因在于每次GC时，所有新生代都会被扫描，所以只需要记录老年代到新生代之间的引用即可
- 需要注意的是，如果引用的对象很多，复制器需要对每个引用做处理，赋值器开销会很大，为了解决赋值器开销这个问题，在G1中又引入了另外一个概念，卡表（Card Table）。一个Card Table将一个分区在逻辑上划分为固定大小的连续区域，每个区域成为卡。卡通常较小，介于128到512字节之间。Card Table通常为字节数组，由Card的索引（即数组下标）来标识每个分区的空间地址
- 默认情况下，每个卡都未被引用。当一个地址空间被引用时，这个地址空间对应的数组索引的值被标记为'0'，即标记为脏被引用，此外RSet也将这个数组下标记录下来。一般情况下，这个RSet其实是一个Hash Table，key是别的Region的起始地址，Value是一个集合，里面的元素是Card Table的Index

- 阶段1：根扫描
  - 静态和本地对象被扫描
- 阶段2：更新RS
  - 处理dirty card队列更新RS
- 阶段3：处理RS
  - 检测从年轻代指向老年代的对象
- 阶段4：对象拷贝
  - 拷贝存活的对象到surivivor/old区域
- 阶段5：处理引用队列
  - 软引用，弱引用，虚引用处理

##### 再谈 Mixed GC

- Mixed GC不仅进行正常的新生代垃圾收集，同时也回收部分后台扫描线程标记的老年代分区
- 它的GC步骤分为两步：
  - 全局并发标记（global concurrent marking）
  - 拷贝存活对象（evacuation）
- 在G1 GC中，global concurrent marking主要是为Mixed GC提供标记服务的，并不是一次GC过程的一个必须环节。 global concurrent marking的执行过程分为四个步骤
  - 初始标记（initial mark，STW）：它标记了从GC Root开始直接可达的对象。
  - 并发标记（Concurrent Marking）：这个阶段从GC Root开始对heap中的对象进行标记，标记线程与应用线程并发执行，并且收集各个Region的存活对象信息
  - 重新标记（Remark，STW）：标记哪些在并发标记阶段发生变化的对象，将被回收
  - 清理（Cleanup）：清除空Region（没有存活对象的），加入到free list

##### 三色标记算法

- 提到并发标记，我们不得不了解并发标记的三色标记算法。它是描述追踪式回收器的一种有效的方法，利用它可以推演回收器的正确性

- 我们将对象分为三种类型：

  - 黑色：根对象，或者该对象与它的子对象都被扫描过（对象被标记了，并且它的所有field也被标记完了）
  - 灰色：对象本身被扫描，但还没扫描完该对象中的子对象（它的field还没有被标记或标记完）
  - 白色：未被扫描对象，扫描完成所有对象之后，最终为白色的为不可达对象，即垃圾对象（对象没有被标记到）

- 根对象被置为黑色，子对象被置为灰色。

  ![](https://i.postimg.cc/Zq9s5CfF/2019-12-17-11-54-17.png)

- 继续有灰色遍历，将已扫描了子对象的对象置为黑色

  ![](https://i.postimg.cc/52W23Czj/2019-12-17-11-56-47.png)

- 遍历了所有可达的对象后，所有可达的对象都变成了黑色。不可达的对象即为白色，需要被清理

![](https://i.postimg.cc/52W23Czj/2019-12-17-11-56-47.png)

- 但是如果在标记过程中，应用程序在运行，那么对象的指针就有可能改变。这样的话，我们就会遇到一个问题：对象丢失问题

- 当垃圾收集器扫描到下面情况时

  ![](https://i.postimg.cc/Qx3yXNGv/2019-12-18-12-04-43.png)

- 这时候应用程序执行以下操作：

  - A.c=C
  - B.c=null

- 这样，对象的状态图变成如下情形：

  ![](https://i.postimg.cc/VkYsbcMV/2019-12-18-12-08-33.png)

- 这时候垃圾收集器再标记扫描的时候就会变成下图这样

![](https://i.postimg.cc/rmhx7JT8/2019-12-18-12-10-36.png)

- 在G1中，使用的是SATB（Snapshot-At-The-Begining）的方式，删除的时候记录所有的对象
- 它有3个步骤
  - 在开始标记的时候生成一个快照图，标记存活对象
  - 在并发标记的时候所有被改变的对象入队（在write barrier里把所有旧的引用所指向对象都变成非白的）
  - 可能存在浮动垃圾，将在下次被收集

##### G1混合式回收

- G1到现在可以知道哪些老的分区可回收垃圾最多。当全局并发标记完成后，在某个时刻，就开始了Mixed GC。这些垃圾回收被称作“混合式“是因为它们不仅仅进行正常的新生代垃圾收集，同时也回收部分后台扫描线程标记的分区
- 混合式GC也是采用的复制清理策略，当GC完成后，会重新释放空间

##### G1分代算法

- 为老年代设置分区的目的是老年代里有的分区垃圾多，有的分区垃圾少，这样在回收的时候可以专注于收集垃圾多分区，这也是G1名称的由来。
- 不过这个算法并不适合新生代垃圾收集，因为新生代的垃圾收集算法是复制算法，但是新生代也使用了分区机制主要是因为便于代大小的调整

##### SATB详解

- SATB是维持并发GC的一种手段。G1并发的基础就是SATB。SATB可以理解成在GC开始之前对堆内存里的对象做一次快照，此时活的对象就认为是活的，从而形成一个对象图。
- 在GC收集的时候，新生代的对象也认为是活的对象，除此之外其他不可达的对象都认为是垃圾对象
- 如何找到在GC过程中分配的对象呢？每个region记录着两个top-mark-start（TAMS）指针，分别为prevTAMS和nextTAMS。在TAMS以上的对象就是分配的，因而被视为隐式marked。
- 通过这种方式我们就找到了在GC过程中新分配的对象，并把这些对象认为是活的对象。
- 解决了对象在GC过程中分配的问题，那么在GC过程中引用发生变化的问题怎么解决呢？
- G1给出的解决办法是通过Write Barrier。Write Barrier就是对引用字段进行赋值做了额外处理。通过Write Barrier就可以了解到哪些引用对象发生了什么样的变化
- mark的过程就是遍历heap标记live object的过程，采用的是三色标记算法，这三种颜色为white（表示还未访问到）、gray（访问到但是它用到的引用还没有完全扫描）、black（访问到而且其用到的引用已经完全扫描完）
- 整个三色标记算法就是从GC roots出发遍历heap，针对可达对象先标记white为gray，然后再标记gray为black；遍历完成之后所有可达对象都是black的，所有white都是可以回收的
- SATB仅仅对于在marking开始阶段进行”snapshot“（marked all reachable at mark start），但是concurrent的时候并发修改可能造成对象漏标记
  - 对black新引用了一个white对象，然后又从gray对象中删除了对该white对象的引用，这样会造成了该white对象漏标记
  - 对black新引用了一个white对象，然后从gray对象删了一个引用该white对象的white对象，这样也会造成了该white对象漏标记
  - 对black新引用了一个刚new出来的white对象，没有其他gray对象引用该white对象，这样也会造成了该white对象漏标记
- 漏标与误标
  - 误标没有什么关系，顶多造成浮动垃圾，在下次gc还是可以回收的，但是漏标的后果是致命的，把本应该存活的对象给回收了，从而影响的程序的正确性
  - 漏标的情况只会发生在白色对象中，且满足以下任意一个条件
    - 并发标记时，应用线程给一个黑色对象的引用类型字段赋值了该白色对象
    - 并发标记时，应用线程删除所有灰色对象到该白色对象的引用
  - 对于第一种情况，利用post-write barrier，记录所有新增的引用关系，然后根据这些引用关系为根重新扫描一遍
  - 对于第二种情况，利用pre-write barrier，将所有即将被删除的引用关系的旧引用记录下来，最后以这些旧引用为根重新扫描一遍

##### 停顿预测模型

- G1收集器突出表现出来的一点是通过一个停顿预测模型根据用户配置的停顿时间来选择CSet的大小，从而达到用户期待的应用程序暂停时间
- 通过-XX:MaxGCPauseMillis参数来设置。这一点有类似于ParallelScavenge收集器。关于停顿时间的设置并不是越短越好
- 设置的时间越短意味着每次收集的CSet越小，导致垃圾逐步积累变多，最终不得不退化成Serial GC；停顿时间设置的过长，那么会导致每次都会产生长时间的停顿，影响了程序对外的响应时间

##### G1的收集模式

- Young GC：收集年轻代里的Region
- Mixed GC：年轻代的所有Region+全局并发标记阶段选出的收益高的Region
- 无论是Young GC还是Mixed GC都只是并发拷贝的阶段
- 分代G1模式下选择CSet的有两种子模式，分别对应Young GC和Mixed GC
- Young GC：CSet就是所有年轻代里面的Region
- Mixed GC：CSet是所有年轻代里的Region加上在全局并发标记阶段标记出来的收益高的Region
- G1的运行过程是这样的：会在Young GC和Mixed GC之间不断地切换运行，同时定期地做全局并发标记，在实在赶不上对象创建速度的情况下使用Full GC（Serial GC）
- 初始标记是在Young GC上执行的，在进行全局并发标记的时候不会做Mixed GC，在做Mixed GC的时候也不会启动初始标记阶段
- 当Mixed GC赶不上对象产生的速度的时候就退化成Full GC，这一点是需要重点调优的地方

##### G1最终实践

- 不断调优暂停时间指标
  - 通过-XX:MaxGCPauseMillis=x可以设置启动应用程序暂停时间，G1在运行的时候会根据这个参数选择CSet来满足响应时间的设置。一般情况下这个值设置到100ms或者200ms都是可以的（不同情况下会不一样），但如果设置成50ms就不太合理。暂停时间设置的太短，就会导致出现G1跟不上垃圾产生的速度。最终退化成Full GC。所以对这个参数的调优是一个持续的过程，逐步调整到最佳状态。
- 不要设置新生代和老年代的大小
  - G1收集器在运行的时候会调整新生代和老年代的大小。通过改变代的大小来调整对象晋升的速度以及晋升年龄，从而达到我们为收集器设置的暂停时间目标。
  - 设置了新生代大小相当于放弃了G1为我们做得自动调优。我们需要做的只是设置整个堆内存的大小，剩下的交给G1自己去分配各个代的大小即可。
- 关注Evacuation Failure
  - Evacuation Failure类似于CMS里面的晋升失败，堆空间的垃圾太多导致无法完成Region之间的拷贝，于是不得不退化成Full GC来做一次全局范围内的垃圾收集