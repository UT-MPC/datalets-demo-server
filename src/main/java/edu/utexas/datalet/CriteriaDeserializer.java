package edu.utexas.datalet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import edu.utexas.datalet.conditions.Condition;

import java.io.IOException;
import java.util.List;

public class CriteriaDeserializer extends JsonDeserializer<Criteria> {

	@Override
	public Criteria deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		Criteria criteria = new Criteria();

		JsonNode treeNode = p.readValueAsTree();

		JsonNode childrenNode = treeNode.get("children");
		JsonNode conditionNode = treeNode.get("condition");
		JsonNode typeNode = treeNode.get("type");

		criteria.setType(typeNode.asText());

		if (childrenNode != null) {
			List<Criteria> children = mapper.convertValue(childrenNode, new TypeReference<List<Criteria>>(){});
			criteria.setChildren(children);
		}

		if (conditionNode != null) {
			Condition condition = mapper.convertValue(conditionNode, Condition.class);
			criteria.setCondition(condition);
		}

		return criteria;
	}
}
