# MybatisDemo

1 导入依赖

<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>3.4.4</version>
</dependency>

<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>5.1.6</version>
</dependency>

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

2 mybatis-config.xml 文件的配置

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">

        	<!-- 配置事务管理 -->
            <transactionManager type="JDBC" />

            <!-- 配置数据库连接信息 -->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver" />
                <property name="url" value="jdbc:mysql://localhost:3306/mybatisdemo" />
                <property name="username" value="root" />
                <property name="password" value="adminmysql" />
            </dataSource>

        </environment>
    </environments>

    <!-- 注册 userMapper.xml 文件  -->
    <mappers>
        <mapper resource="userMapper.xml"/>
    </mappers>

</configuration>

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

3 准备 SQL映射文件

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="me.gacl.mapping.userMapper">
    <select id="getUser" parameterType="int" resultType="me.gacl.domain.User">
        select * from users where id=#{id}
    </select>
</mapper>

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

4 执行SQL    [基于XML实现]

//1：加载配置文件
String resource = "mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);

//2：获取sqlSession对象
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
SqlSession sqlSession = sqlSessionFactory.openSession();

//3:加载SQL映射文件
String SQLMapping = "userMapper.getUser";

//根据SQL映射文件返回对象
User user = sqlSession.selectOne(SQLMapping, 1);
System.out.print(user);

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

5 执行SQL    [基于注解实现]

1：准备接口

public interface UserMapperImpl {

    @Insert("insert into user values(#{id},#{name},#{age})")
    public int AddUser(User user);

    @Select("select * from user where id=#{id}")
    public User retrieveUser(int id);

    @Select("select * from user")
    public List<User> retrieveAllUser();

    @Update("update user set name=#{name} where id=#{id}")
    public int updateUser(User user);

    @Delete("delete from user where name=#{name}")
    public int deleteUser(String name);

}

2：实例化映射类

    SqlSession sqlSession = MybatisUtil.getSqlSession(true);
    UserMapperImpl annotationImplTool = sqlSession.getMapper(UserMapperImpl.class);

3：根据接口中的方法执行SQL
	int addResult = annotationImplTool.AddUser(user);

4：要注意的事情
对于简单语句来说，注解使代码显得更加简洁，然而 Java 注解对于稍微复杂的语句就会力不从心并且会显得更加混乱。因此，如果你需要做很复杂的事情，那么最好使用 XML 来映射语句。

选择何种方式以及映射语句的定义的一致性对你来说有多重要这些完全取决于你和你的团队。
换句话说，永远不要拘泥于一种方式，你可以很轻松的在基于注解和XML的语句映射方式间自由移植和切换。

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

6 命名空间    [namespace]

1：WHERE
ClassMapper.xml
<mapper namespace="userMapper">
    <insert id="addUser" parameterType="com.gs.mybatis.bean.User">
        insert into user(name,age) values(#{name},#{age})
    </insert>
</mapper>

2：WHEN
String statement = "userMapper.addUser";
int insertResult = sqlSession.insert(statement, user);

3：HOW
sqlSession中执行 SQL 操作时，通过 statement 到 Mybatis-config.xml 中的 Mappers 中查找 对应的 Mapper 然后到 指定的 Mapper文件中 找到指定 id 的 SQL 映射

4：要注意的事情

为了防止不同包下相同类名的冲突，请使用如下明明规则
完全限定名=包名称+类名称[接口名称]（比如“com.mypackage.MyMapper.selectAllThings”）将被直接查找并且找到即用。

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

7 最佳作用域和生命周期

1：SqlSessionFactoryBuilder

1.1 在哪里会出现?
SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder().build(ResourceIS);

1.2 有什么作用
产生 SqlSessionFactory 对象

1.3 最佳作用域
在局部方法中实例化完成后即可丢弃

1.4 要注意的事情
你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例
但是最好还是不要让其一直存在以保证所有的 XML 解析资源开放给更重要的事情    [因为实例化 SqlSessionFactory 对象需要传入 Mybatis-config.xml 文件输入流对象]


2：SqlSessionFactory

2.1 在哪里会出现?
SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder().build(ResourceIS);

2.2 有什么作用
产生 SqlSession 对象

2.3 最佳作用域
一旦被创建就应该在应用的运行期间一直存在

2.4 要注意的事情
因为 SqlSession 不是线程安全的对象，也就意味着随着多线程的产生需要不断的产生 SqlSession 对象。而该对象的产生就是依赖 SqlSessionFactory 对象


3：SqlSession

3.1 在哪里会出现?
SqlSession sqlsession = Resources.openSession(true);

3.2 有什么作用
执行 SQL 操作

3.3 最佳作用域
请求或方法作用域

3.4 要注意的事情
每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的。
绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。
也绝不能将 SqlSession 实例的引用放在任何类型的管理作用域中，比如 Servlet 架构中的 HttpSession。
如果你现在正在使用一种 Web 框架，要考虑 SqlSession 放在一个和 HTTP 请求对象相似的作用域中。
换句话说，每次收到的 HTTP 请求，就可以打开一个 SqlSession，返回一个响应，就关闭它。
这个关闭操作是很重要的，你应该把这个关闭操作放到 finally 块中以确保每次都能执行关闭。下面的示例就是一个确保 SqlSession 关闭的标准模式：

SqlSession session = sqlSessionFactory.openSession();
try {
  // work code
} finally {
  session.close();
}

























