package com.codingspace.freecoin.helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ChartHelper {
	
	static Map<Object,Root> map = new HashMap<Object,Root>();

	public static void main(String[] args) throws Exception {
        String file = "src/main/resources/node_collection.json";
        String json = readFileAsString(file);
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper objectMapper = new ObjectMapper(factory);
        List<Root> rootList = objectMapper.readValue(json,objectMapper.getTypeFactory().constructCollectionType(List.class, Root.class));
        for(Root root : rootList)
        {
        	map.put(root.getUserId(),root);
        }
        Output result = createTreeNode(rootList.get(0));
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("src/main/resources/result.json"), result);
    }
    private static Output createTreeNode(Root root) {
		Output output =null;
		if(root != null)
		{
			output = new Output();
			output.setUser(root);
			List<Output> child = output.getChildrens();
			if(root.getLeft() != null)
			{
				Output leftNode = createTreeNode(map.get(root.getLeft().getUserId()));
				child.add(leftNode);
			}
			if(root.getRight() != null)
			{
				Output rightNode = createTreeNode(map.get(root.getRight().getUserId()));
				child.add(rightNode);
			}
			output.setChildrens(child);
		}
		return output;
	}
	public static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
