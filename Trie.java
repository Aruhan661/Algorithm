package name.aruhan.datastructure;

import java.util.Map;
import java.util.Set;

/**
 * 字符串前缀树接口，用于实现基于字符串的键值存储和检索。
 *
 * @param <Value> 值的类型
 * @author Aruhan
 * @since 2023-12-14
 */
public interface Trie<Value>{
    /**
     * 将键值对插入到前缀树中。如果键已存在，则更新相关联的值。
     *
     * @param key   键
     * @param value 值
     */
    void put(String key, Value value);

    /**
     * 在前缀树中检索与指定键相关联的值。
     *
     * @param key 键
     * @return 与指定键相关联的值，若键不存在则为 null
     */
    Value get(String key);

    /**
     * 从前缀树中删除键及其关联的值。如果成功移除键，则返回 true；否则，返回 false。
     *
     * @param key 键
     * @return 是否成功移除
     */
    boolean remove(String key);

    /**
     * 检查前缀树是否包含指定的键。如果键存在，则返回 true；否则，返回 false。
     *
     * @param key 键
     * @return 是否包含指定键
     */
    boolean containsKey(String key);

    /**
     * 清除前缀树中的所有键值对。
     */
    void clear();

    /**
     * 检查前缀树是否为空。如果前缀树不包含任何元素，则返回 true；否则，返回 false。
     *
     * @return 前缀树是否为空
     */
    boolean isEmpty();

    /**
     * 返回前缀树中键值对的数量。
     *
     * @return 前缀树中键值对的数量
     */
    int size();

    /**
     * 返回前缀树中包含的键值映射的集合。
     *
     * @return 键值映射的集合
     */
    Set<Map.Entry<String, Value>> entrySet();

    /**
     * 返回一个集合，其中包含以指定前缀开头的键值对。
     *
     * @param prefix 指定的前缀字符串
     * @return 以指定前缀开头的键值映射的集合
     */
    Set<Map.Entry<String, Value>> startsWith(String prefix);

    /**
     * 返回前缀树中包含的键的集合。
     *
     * @return 前缀树中包含的键的集合
     */
    Set<String> keySet();
}
