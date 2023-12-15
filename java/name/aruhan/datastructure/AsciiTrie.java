package name.aruhan.datastructure;

import java.util.*;

/**
 * ASCII字符串前缀树接口，实现了Trie接口
 *
 * @param <Value> 值的类型
 * @author Aruhan
 * @since 2023-12-14
 */
public class AsciiTrie<Value> implements Trie<Value> {
    // Trie节点类，包含子节点数组、值、结束标志
    private static class TrieNode<Value> {
        private Value value;
        private boolean isEnd;
        private final TrieNode<Value>[] children;
        private int numChildNodes;

        @SuppressWarnings("unchecked")
        public TrieNode(Value value) {
            this.isEnd = false;
            this.value = value;
            this.children = (TrieNode<Value>[]) new TrieNode[CHARACTER_COUNT];
            this.numChildNodes = 0;
        }
    }

    // 常量，表示ASCII字符的数量
    private static final int CHARACTER_COUNT = 128;

    // Trie树的根节点
    private final TrieNode<Value> root;

    // 构造函数，初始化Trie树
    public AsciiTrie() {
        root = new TrieNode<>(null);
    }

    /**
     * 将键值对插入到前缀树中。如果键已存在，则更新相关联的值。
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void put(String key, Value value) {
        if (key == null)
            return;
        TrieNode<Value> node = root;
        for (char ch : key.toCharArray()) {
            if (ch >= 128)
                throw new IllegalArgumentException("key string contains non-ascii characters");
            if (node.children[ch] == null) {
                node.children[ch] = new TrieNode<>(null);
                node.numChildNodes++;
            }
            node = node.children[ch];
        }
        node.isEnd = true;
        node.value = value;
    }

    /**
     * 在前缀树中检索与指定键相关联的值。
     *
     * @param key 键
     * @return 与指定键相关联的值，若键不存在则为 null
     */
    @Override
    public Value get(String key) {
        if (key == null)
            return null;
        TrieNode<Value> node = getNode(key);
        return (node != null && node.isEnd) ? node.value : null;
    }

    /**
     * 从前缀树中删除键及其关联的值。如果成功移除键，则返回 true；否则，返回 false。
     *
     * @param key 键
     * @return 是否成功移除
     */
    @Override
    public boolean remove(String key) {
        if (key == null)
            return false;
        if (key.equals("")) {
            if (root.isEnd) {
                root.isEnd = false;
                return true;
            } else return false;
        }

        TrieNode<Value> node = root;
        Stack<TrieNode<Value>> parentStack = new Stack<>();

        for (char ch : key.toCharArray()) {
            if (ch >= 128)
                throw new IllegalArgumentException("key string contains non-ascii characters");

            parentStack.push(node);
            node = node.children[ch];
            if (node == null)
                return false;
        }

        node.isEnd = false;
        node.value = null;

        int index = key.length();
        if (node.numChildNodes == 0) {
            while (!parentStack.isEmpty()) {
                TrieNode<Value> parent = parentStack.pop();
                char ch = key.charAt(--index);
                parent.children[ch] = null;
                parent.numChildNodes--;

                if (parent.numChildNodes > 0 || parent.isEnd)
                    break;
            }
        }
        return true;
    }

    /**
     * 检查前缀树是否包含指定的键。如果键存在，则返回 true；否则，返回 false。
     *
     * @param key 键
     * @return 是否包含指定键
     */
    @Override
    public boolean containsKey(String key) {
        if (key == null)
            return false;
        TrieNode<Value> node = getNode(key);
        return node != null && node.isEnd;
    }

    /**
     * 清除前缀树中的所有键值对。
     */
    @Override
    public void clear() {
        root.isEnd = false;
        root.numChildNodes = 0;
        Arrays.fill(root.children, null);
    }

    /**
     * 检查前缀树是否为空。如果前缀树不包含任何元素，则返回 true；否则，返回 false。
     *
     * @return 前缀树是否为空
     */
    @Override
    public boolean isEmpty() {
        return root.numChildNodes == 0 && !root.isEnd;
    }

    /**
     * 返回前缀树中键值对的数量。
     *
     * @return 前缀树中键值对的数量
     */
    @Override
    public int size() {
        Set<String> keySet = new HashSet<>();
        collectKeys(root, new StringBuilder(), keySet);
        return keySet.size();
    }

    /**
     * 返回前缀树中包含的键值映射的集合。
     *
     * @return 键值映射的集合
     */
    @Override
    public Set<Map.Entry<String, Value>> entrySet() {
        Set<Map.Entry<String, Value>> entrySet = new HashSet<>();
        collectEntries(root, new StringBuilder(), entrySet);
        return entrySet;
    }

    /**
     * 返回一个集合，其中包含以指定前缀开头的键值对。
     *
     * @param prefix 指定的前缀字符串
     * @return 以指定前缀开头的键值映射的集合
     */
    @Override
    public Set<Map.Entry<String, Value>> startsWith(String prefix) {
        Set<Map.Entry<String, Value>> startsWithSet = new HashSet<>();
        TrieNode<Value> startNode = getNode(prefix);
        if (startNode != null)
            collectEntries(startNode, new StringBuilder(prefix), startsWithSet);
        return startsWithSet;
    }

    /**
     * 返回前缀树中包含的键的集合。
     *
     * @return 前缀树中包含的键的集合
     */
    @Override
    public Set<String> keySet() {
        Set<String> keySet = new HashSet<>();
        collectKeys(root, new StringBuilder(), keySet);
        return keySet;
    }

    // 辅助方法，递归地收集键
    private void collectKeys(TrieNode<Value> node, StringBuilder currentKey, Set<String> keySet) {
        if (node == null)
            return;
        if (node.isEnd)
            keySet.add(currentKey.toString());
        for (char ch = 0; ch < CHARACTER_COUNT; ch++) {
            if (node.children[ch] != null) {
                collectKeys(node.children[ch], currentKey.append(ch), keySet);
                currentKey.deleteCharAt(currentKey.length() - 1);
            }
        }
    }

    // 辅助方法，递归地收集键值对
    private void collectEntries(TrieNode<Value> node, StringBuilder currentKey, Set<Map.Entry<String, Value>> entrySet) {
        if (node == null)
            return;
        if (node.isEnd)
            entrySet.add(new AbstractMap.SimpleEntry<>(currentKey.toString(), node.value));
        for (char ch = 0; ch < CHARACTER_COUNT; ch++) {
            if (node.children[ch] != null) {
                collectEntries(node.children[ch], currentKey.append(ch), entrySet);
                currentKey.deleteCharAt(currentKey.length() - 1);
            }
        }
    }

    // 辅助方法，获取指定键的节点
    private TrieNode<Value> getNode(String key) {
        if (key == null)
            return null;
        TrieNode<Value> node = root;
        for (char ch : key.toCharArray()) {
            if (ch >= 128)
                throw new IllegalArgumentException("key string contains non-ascii characters");
            node = node.children[ch];
            if (node == null)
                return null;
        }
        return node;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.entrySet());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        AsciiTrie<?> other = (AsciiTrie<?>) obj;
        return this.entrySet().equals(other.entrySet());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("AsciiTrie {");
        Set<Map.Entry<String, Value>> entrySet = entrySet();
        for (Map.Entry<String, Value> entry : entrySet)
            result.append("\n  \"").append(entry.getKey()).append("\": ").append(entry.getValue());
        result.append("\n}");
        return result.toString();
    }
}
